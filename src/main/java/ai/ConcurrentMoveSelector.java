/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Nick Houser
 */
public class ConcurrentMoveSelector {

    private static final int NUM_THREADS = 8;

    private ExecutorService executor;

    public ConcurrentMoveSelector() {
        executor = Executors.newFixedThreadPool(NUM_THREADS);

    }
}
