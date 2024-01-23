package com.tournament.pointstabletracker.observers;

import com.tournament.pointstabletracker.dto.AddMatchResultRequest;
import com.tournament.pointstabletracker.entity.app.PointsTable;
import com.tournament.pointstabletracker.entity.app.TeamStats;
import com.tournament.pointstabletracker.exceptions.InvalidRequestException;
import com.tournament.pointstabletracker.exceptions.RecordNotFoundException;
import com.tournament.pointstabletracker.repository.PointsTableRepository;
import com.tournament.pointstabletracker.repository.TeamStatsRepository;
import com.tournament.pointstabletracker.utils.ApplicationConstants;
import com.tournament.pointstabletracker.utils.NetRunRateCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.tournament.pointstabletracker.utils.ApplicationConstants.NO_POINTS_FOR_WIN;

@Component
public class PointsTableObserver implements MatchResultObserver {
    private final PointsTableRepository pointsTableRepository;

    private final TeamStatsRepository teamStatsRepository;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(PointsTableObserver.class);


    @Autowired
    public PointsTableObserver(PointsTableRepository pointsTableRepository, TeamStatsRepository teamStatsRepository) {
        this.pointsTableRepository = pointsTableRepository;
        this.teamStatsRepository = teamStatsRepository;
    }

    @Override
    public void update(AddMatchResultRequest matchResultRequest) throws RecordNotFoundException, InvalidRequestException {

        logger.info("Updating points table for match result: {} {}", matchResultRequest.getTeamOneId(), matchResultRequest.getTeamTwoId());

        long tournamentId = matchResultRequest.getTournamentId();
        long teamOneId = matchResultRequest.getTeamOneId();
        long teamTwoId = matchResultRequest.getTeamTwoId();

        TeamStats teamOneStats = teamStatsRepository.findByTeamIdAndTournamentId(teamOneId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No stats found with teamId:" + teamOneId + "and tournamentId: " + tournamentId));

        TeamStats teamTwoStats = teamStatsRepository.findByTeamIdAndTournamentId(teamTwoId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No stats found with teamId:" + teamTwoId + "and tournamentId: " + tournamentId));

        // update points table
        PointsTable teamOneMatchStats = pointsTableRepository.findByTeamIdAndTournamentId(teamOneId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No points record found with teamId:" + teamOneId + "and tournamentId: " + tournamentId));
        PointsTable teamTwoMatchStats = pointsTableRepository.findByTeamIdAndTournamentId(teamTwoId, tournamentId).orElseThrow(() -> new RecordNotFoundException("No points record found with teamId:" + teamTwoId + "and tournamentId: " + tournamentId));

        teamOneMatchStats.setPlayed(teamOneMatchStats.getPlayed() + 1);
        teamTwoMatchStats.setPlayed(teamTwoMatchStats.getPlayed() + 1);

        teamTwoMatchStats.setRecordUpdatedDate(LocalDateTime.now());
        teamOneMatchStats.setRecordUpdatedDate(LocalDateTime.now());

        double netRunRateTeamOne = NetRunRateCalculator.calculateNetRunRate(teamOneStats.getTotalRunsScored(), teamOneStats.getTotalTeamOversPlayed(), teamOneStats.getTotalRunsConceded(), teamOneStats.getTotalOversBowled());
        double netRunRateTeamTwo = NetRunRateCalculator.calculateNetRunRate(teamTwoStats.getTotalRunsScored(), teamTwoStats.getTotalTeamOversPlayed(), teamTwoStats.getTotalRunsConceded(), teamTwoStats.getTotalOversBowled());

        if (matchResultRequest.getMatchResultStatus() == ApplicationConstants.MatchResultStatus.COMPLETED) {

            long winnerTeamId = matchResultRequest.getWinnerTeamId();

            if (winnerTeamId == teamOneId) {

                teamOneMatchStats.setWon(teamOneMatchStats.getWon() + 1);
                teamOneMatchStats.setPoints(teamOneMatchStats.getPoints() + NO_POINTS_FOR_WIN);

                teamTwoMatchStats.setLost(teamTwoMatchStats.getLost() + 1);

            } else if (winnerTeamId == teamTwoId) {

                teamTwoMatchStats.setWon(teamTwoMatchStats.getWon() + 1);
                teamTwoMatchStats.setPoints(teamTwoMatchStats.getPoints() + NO_POINTS_FOR_WIN);

                teamOneMatchStats.setLost(teamOneMatchStats.getLost() + 1);

            }

            teamOneMatchStats.setNetMatchRate(netRunRateTeamOne);
            teamTwoMatchStats.setNetMatchRate(netRunRateTeamTwo);

        } else if (matchResultRequest.getMatchResultStatus() == ApplicationConstants.MatchResultStatus.TIED) {
            teamOneMatchStats.setTied(teamOneMatchStats.getTied() + 1);
            teamTwoMatchStats.setTied(teamTwoMatchStats.getTied() + 1);

            teamOneMatchStats.setNetMatchRate(netRunRateTeamOne);
            teamTwoMatchStats.setNetMatchRate(netRunRateTeamTwo);
        } else if (matchResultRequest.getMatchResultStatus() == ApplicationConstants.MatchResultStatus.NO_RESULT) {
            teamOneMatchStats.setNoResult(teamOneMatchStats.getNoResult() + 1);
            teamTwoMatchStats.setNoResult(teamTwoMatchStats.getNoResult() + 1);
        }

        pointsTableRepository.saveAll(List.of(teamOneMatchStats, teamTwoMatchStats));

        logger.info("Points table updated successfully for teamId: {} and teamId: {}", teamOneId, teamTwoId);

    }
}

