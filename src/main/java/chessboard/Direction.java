/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessboard;

/**
 * Enumeration of the straight line directions on a chessboard.
 *
 * @author Nick Houser
 */
public enum Direction {
    NORTH {
        @Override
        public int x() {
            return 0;
        }

        @Override
        public int y() {
            return -1;
        }
    },
    NORTHEAST {
        @Override
        public int x() {
            return 1;
        }

        @Override
        public int y() {
            return -1;
        }
    },
    EAST {
        @Override
        public int x() {
            return 1;
        }

        @Override
        public int y() {
            return 0;
        }
    },
    SOUTHEAST {
        @Override
        public int x() {
            return 1;
        }

        @Override
        public int y() {
            return 1;
        }
    },
    SOUTH {
        @Override
        public int x() {
            return 0;
        }

        @Override
        public int y() {
            return 1;
        }
    },
    SOUTHWEST {
        @Override
        public int x() {
            return -1;
        }

        @Override
        public int y() {
            return 1;
        }
    },
    WEST {
        @Override
        public int x() {
            return -1;
        }

        @Override
        public int y() {
            return 0;
        }
    },
    NORTHWEST {
        @Override
        public int x() {
            return -1;
        }

        @Override
        public int y() {
            return -1;
        }
    };

    /**
     * Method which returns the x component of the direction.
     *
     * @return the x component of the direction
     */
    public abstract int x();

    /**
     * Method which returns the y component of the direction.
     *
     * @return the y component of the direction
     */
    public abstract int y();
}
