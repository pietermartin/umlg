package org.tuml.runtime.domain.activity;

import java.util.Collections;
import java.util.List;

import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.domain.BaseTinkerBehavioredClassifier;
import org.tuml.runtime.domain.IClassifierSignalEvent;
import org.tuml.runtime.domain.ISignal;
import org.tuml.runtime.domain.activity.interf.IOutputPin;
import org.tuml.runtime.domain.activity.interf.ISendSignalAction;
import org.tuml.runtime.util.TinkerClassifierBehaviorExecutorService;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import com.tinkerpop.blueprints.Vertex;

public abstract class SendSignalAction extends InvocationAction implements ISendSignalAction {

	private static final long serialVersionUID = 3573865245792011088L;

	public SendSignalAction() {
		super();
	}

	public SendSignalAction(boolean persist, String name) {
		super(persist, name);
	}

	public SendSignalAction(Vertex vertex) {
		super(vertex);
	}
	
	@Override
	protected boolean execute() {
		logger.finest(String.format("executing action {0}", getClass().getSimpleName()));
		TinkerClassifierBehaviorExecutorService.INSTANCE.submit(new IClassifierSignalEvent() {
			@Override
			public Boolean call() throws Exception {
				//TODO huge todo dude, synchronize this to only start once previous calling thread's transaction commits
				Thread.sleep(1000);
				try {
					resolveTarget().receiveSignal(getSignal());
					GraphDb.getDb().commit();
				} catch (Exception e) {
					GraphDb.getDb().rollback();
					throw e;
				}
				return true;
			}

		});
		return true;
	}

	protected abstract BaseTinkerBehavioredClassifier resolveTarget();

	@Override
	public abstract ISignal getSignal();
	
	@Override
	public List<? extends IOutputPin<?,?>> getOutput() {
		return Collections.emptyList();
	}
	
}
