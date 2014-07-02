package org.umlg.runtime.adaptor;

import com.tinkerpop.gremlin.structure.Vertex;
import com.tinkerpop.gremlin.structure.strategy.Strategy;
import com.tinkerpop.gremlin.structure.strategy.StrategyWrappedGraph;
import com.tinkerpop.gremlin.structure.strategy.StrategyWrappedVertex;

/**
 * Date: 2014/06/26
 * Time: 8:18 PM
 */
public class UmlgStrategyWrappedVertex extends StrategyWrappedVertex {

    private final Vertex baseVertex;
    private final Strategy.Context<StrategyWrappedVertex> strategyContext;

    public UmlgStrategyWrappedVertex(Vertex baseVertex, StrategyWrappedGraph strategyWrappedGraph) {
        super(baseVertex, strategyWrappedGraph);
        this.strategyContext = new Strategy.Context<>(strategyWrappedGraph.getBaseGraph(), this);
        this.baseVertex = baseVertex;
    }

    @Override
    public void remove() {
        this.strategyWrappedGraph.strategy().compose(
                s -> s.getRemoveElementStrategy(elementStrategyContext),
                () -> null ).get();
    }

}
