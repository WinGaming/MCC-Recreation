package mcc.display;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;

import mcc.scores.Score;
import mcc.scores.Scorelist;
import mcc.utils.Pair;

public class ScorelistScoreboardPartProvider<T extends Score<T, V>, V> implements ScoreboardPartProvider {
	
    private boolean showAbsoluteFirst = false; // TODO: Make this configurable

    // TODO: Make this configurable
    // private int lineLength = 35;

	private Scorelist<T> scores;

    public ScorelistScoreboardPartProvider(Scorelist<T> scores) {
        this.scores = scores;
    }

    private String createScorelistLine(UUID uuid, int displayIndexLength, int displayIndex, Score<T,V> score, Optional<T> viewerScore) {
        String indexString = displayIndex + ".";
        String name = Bukkit.getPlayer(uuid) != null ? Bukkit.getPlayer(uuid).getName() : uuid.toString();
        String scoreString = score.toScoreboardText(viewerScore);

        int indexWidth = SpaceFont.getWidthOf(indexString);

        final int totalLength = 128;
        final int indexNameOffset = 5;

        String line = "";
        line += SpaceFont.getSpaceString(displayIndexLength - indexWidth) + indexString + SpaceFont.getSpaceString(-displayIndexLength);
        line += SpaceFont.getSpaceString(indexWidth + indexNameOffset) + name + SpaceFont.getSpaceString(-(indexWidth + indexNameOffset) + -SpaceFont.getWidthOf(name));
        line += SpaceFont.getSpaceString(totalLength - SpaceFont.getWidthOf(scoreString)) + scoreString;

        return line;
    }

    private int getLongestIndex(Scorelist<T> scores) {
        // final boolean onlyUseDisplayedScores = false; // MCC uses false, true does not make sense to me
        return SpaceFont.getWidthOf('.') + (SpaceFont.getWidthOf('0') * (scores.size() + "").length());
    }

    @Override
    public Pair<String[], Long> getLines(UUID viewer) {
        List<Pair<UUID, T>> sortedScores = scores.sortScores();
        if (sortedScores.size() == 0) {
            return new Pair<>(new String[] {""}, 0L);
        }
        
        Optional<T> viewerScore = Optional.empty();
        int viewerScoreIndex = -1;

        for (int i = 0; i < sortedScores.size(); i++) {
            if (sortedScores.get(i).getA().equals(viewer)) {
                viewerScoreIndex = i;
                viewerScore = Optional.of(sortedScores.get(i).getB());
                break;
            }
        }

        int longestIndexLength = getLongestIndex(scores);

        String[] lines;
        
        if (this.showAbsoluteFirst) {
            final int n = 3;
            lines = new String[n + 2];
            // fill with empty lines
            Arrays.fill(lines, " ");
            
            // TODO: MCC displays total time if viewer is first, but this feels like a bug
            lines[0] = createScorelistLine(sortedScores.get(0).getA(), longestIndexLength, 1, sortedScores.get(0).getB(), viewerScore) + SpaceFont.NEG_SPACE_1 + SpaceFont.POS_SPACE_1; // Making sure if viewer is 1. it can display the same line twice
            lines[1] = "-";
            
            Bukkit.broadcastMessage("viewerScoreIndex: " + viewerScoreIndex);
            int topNStartIndex = Math.max(0, Math.min(viewerScoreIndex - 1, sortedScores.size() - n));
            for (int i = 0; i < n; i++) {
                if (topNStartIndex + i >= sortedScores.size()) break;
                Pair<UUID, T> scorePair = sortedScores.get(topNStartIndex + i);
                lines[2 + i] = createScorelistLine(scorePair.getA(), longestIndexLength, topNStartIndex + i + 1, scorePair.getB(), viewerScore);
            }
        } else {
            final int n = 3;
            lines = new String[n + 1];
            // fill with empty lines
            Arrays.fill(lines, " ");

            lines[0] = createScorelistLine(sortedScores.get(0).getA(), longestIndexLength, 1, sortedScores.get(0).getB(), viewerScore);
            
            int topNStartIndex = Math.max(1, Math.min(viewerScoreIndex - 1, sortedScores.size() - n));
            for (int i = 0; i < n; i++) {
                if (topNStartIndex + i >= sortedScores.size()) break;
                Pair<UUID, T> scorePair = sortedScores.get(topNStartIndex + i);
                lines[1 + i] = createScorelistLine(scorePair.getA(), longestIndexLength, topNStartIndex + i + 1, scorePair.getB(), viewerScore);
            }
        }

        return new Pair<String[],Long>(lines, System.currentTimeMillis()); // TODO: Not always update?
    }
}
