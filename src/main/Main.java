package main;

import factory.Strategy;
import gameboard.GameBoard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public final class Main {

    private static final class GameInputLoader {
        private final String mInputPath;

        private GameInputLoader(final String path) {
            mInputPath = path;
        }

        public GameInput load() {
            List<Integer> assetsIds = new ArrayList<>();
            List<String> playerOrder = new ArrayList<>();

            try {
                BufferedReader inStream = new BufferedReader(new FileReader(mInputPath));
                String assetIdsLine = inStream.readLine().replaceAll("[\\[\\] ']", "");
                String playerOrderLine = inStream.readLine().replaceAll("[\\[\\] ']", "");

                for (String strAssetId : assetIdsLine.split(",")) {
                    assetsIds.add(Integer.parseInt(strAssetId));
                }

                for (String strPlayer : playerOrderLine.split(",")) {
                    playerOrder.add(strPlayer);
                }
                inStream.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return new GameInput(assetsIds, playerOrder);
        }
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0]);
        GameInput gameInput = gameInputLoader.load();

        Queue<Integer> deck = new LinkedList<>(gameInput.getAssetIds());
        Strategy[] strategies = new Strategy[gameInput.getPlayerNames().size()];
        for (int i = 0; i < strategies.length; ++i) {
            if (gameInput.getPlayerNames().get(i).equals("basic")) {
                strategies[i] = Strategy.BASIC;
            }
            if (gameInput.getPlayerNames().get(i).equals("greedy")) {
                strategies[i] = Strategy.GREEDY;
            }
            if (gameInput.getPlayerNames().get(i).equals("bribed")) {
                strategies[i] = Strategy.BRIBE;
            }
        }
        GameBoard gameBoard = new GameBoard(strategies, deck);
        GameBoard.playGame();
    }
}
