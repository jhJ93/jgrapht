/*
 * (C) Copyright 2018-2018, by Timofey Chudakov and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package org.jgrapht.alg.color;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.cycle.ChordalityInspector;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests for the {@link ChordalGraphColoring}
 *
 * @author Timofey Chudakov
 */
public class ChordalGraphColoringTest {
    /**
     * Tests coloring of an empty graph
     */
    @Test
    public void testGetColoring1() {
        Graph<Integer, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        VertexColoringAlgorithm.Coloring<Integer> coloring = new ChordalGraphColoring<>(graph).getColoring();
        assertNotNull(coloring);
        assertEquals(0, coloring.getNumberColors());
        assertEquals(0, coloring.getColors().size());
        assertEquals(0, coloring.getColorClasses().size());
    }

    /**
     * Tests coloring on a small clique
     */
    @Test
    public void testGetColoring2() {
        Graph<Integer, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        Graphs.addEdgeWithVertices(graph, 1, 2);
        Graphs.addEdgeWithVertices(graph, 1, 3);
        Graphs.addEdgeWithVertices(graph, 2, 3);
        VertexColoringAlgorithm.Coloring<Integer> coloring = new ChordalGraphColoring<>(graph).getColoring();
        assertNotNull(coloring);
        assertEquals(3, coloring.getNumberColors());
        assertIsColoring(graph, coloring);
    }

    /**
     * Tests coloring on a non-chordal graph.
     */
    @Test
    public void testGetColoring3() {
        Graph<Integer, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        Graphs.addEdgeWithVertices(graph, 1, 2);
        Graphs.addEdgeWithVertices(graph, 1, 3);
        Graphs.addEdgeWithVertices(graph, 2, 4);
        Graphs.addEdgeWithVertices(graph, 3, 4);
        VertexColoringAlgorithm.Coloring<Integer> coloring = new ChordalGraphColoring<>(graph).getColoring();
        assertNull(coloring);
    }

    /**
     * Tests coloring of the big graph
     */
    @Test
    public void testGetColoring4() {
        Graph<Integer, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        Graphs.addEdgeWithVertices(graph, 1, 2);
        Graphs.addEdgeWithVertices(graph, 2, 3);
        Graphs.addEdgeWithVertices(graph, 3, 4);
        Graphs.addEdgeWithVertices(graph, 4, 5);
        Graphs.addEdgeWithVertices(graph, 5, 6);
        Graphs.addEdgeWithVertices(graph, 6, 7);
        Graphs.addEdgeWithVertices(graph, 7, 8);
        Graphs.addEdgeWithVertices(graph, 8, 9);
        Graphs.addEdgeWithVertices(graph, 9, 10);
        Graphs.addEdgeWithVertices(graph, 10, 1);
        Graphs.addEdgeWithVertices(graph, 2, 4);
        Graphs.addEdgeWithVertices(graph, 4, 6);
        Graphs.addEdgeWithVertices(graph, 6, 8);
        Graphs.addEdgeWithVertices(graph, 8, 10);
        Graphs.addEdgeWithVertices(graph, 10, 2);
        Graphs.addEdgeWithVertices(graph, 2, 6);
        Graphs.addEdgeWithVertices(graph, 2, 8);
        Graphs.addEdgeWithVertices(graph, 4, 8);
        Graphs.addEdgeWithVertices(graph, 4, 10);
        Graphs.addEdgeWithVertices(graph, 6, 10);
        VertexColoringAlgorithm.Coloring<Integer> coloring = new ChordalGraphColoring<>(graph).getColoring();
        assertNotNull(coloring);
        assertIsColoring(graph, coloring);
        assertEquals(5, coloring.getNumberColors());
    }

    /**
     * Tests coloring of a pseudograph
     */
    @Test
    public void testGetColoring5() {
        Graph<Integer, DefaultEdge> graph = new DefaultUndirectedGraph<>(DefaultEdge.class);
        Graphs.addEdgeWithVertices(graph, 1, 1);
        Graphs.addEdgeWithVertices(graph, 2, 2);
        Graphs.addEdgeWithVertices(graph, 2, 3);
        Graphs.addEdgeWithVertices(graph, 2, 3);
        Graphs.addEdgeWithVertices(graph, 2, 4);
        Graphs.addEdgeWithVertices(graph, 3, 4);
        Graphs.addEdgeWithVertices(graph, 3, 4);
        Graphs.addEdgeWithVertices(graph, 3, 4);
        Graphs.addEdgeWithVertices(graph, 4, 4);
        Graphs.addEdgeWithVertices(graph, 4, 4);
        Graphs.addEdgeWithVertices(graph, 5, 5);
        Graphs.addEdgeWithVertices(graph, 5, 5);
        VertexColoringAlgorithm.Coloring<Integer> coloring = new ChordalGraphColoring<>(graph).getColoring();
        assertNotNull(coloring);
        assertIsColoring(graph, coloring);
        assertEquals(3, coloring.getNumberColors());
    }

    /**
     * Checks whether the {@code coloring} is a valid vertex coloring.
     *
     * @param graph    the tested graph.
     * @param coloring the tested coloring.
     * @param <V>      the graph vertex type.
     * @param <E>      the graph edge type.
     */
    private <V, E> void assertIsColoring(Graph<V, E> graph, VertexColoringAlgorithm.Coloring<V> coloring) {
        Map<V, Integer> colors = coloring.getColors();
        for (V vertex : graph.vertexSet()) {
            for (E edge : graph.edgesOf(vertex)) {
                V opposite = Graphs.getOppositeVertex(graph, edge, vertex);
                if (!vertex.equals(opposite)) {
                    assertNotEquals(colors.get(vertex), colors.get(opposite));
                }
            }
        }
    }
}
