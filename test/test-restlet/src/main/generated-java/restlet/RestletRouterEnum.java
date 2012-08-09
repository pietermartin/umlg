package restlet;

import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.tuml.test.restlet.FingerServerResourceImpl;
import org.tuml.test.restlet.Finger_hand_ServerResourceImpl;
import org.tuml.test.restlet.Finger_ring_ServerResourceImpl;
import org.tuml.test.restlet.HandServerResourceImpl;
import org.tuml.test.restlet.Hand_finger_ServerResourceImpl;
import org.tuml.test.restlet.Hand_human_ServerResourceImpl;
import org.tuml.test.restlet.HumanServerResourceImpl;
import org.tuml.test.restlet.Human_hand_ServerResourceImpl;
import org.tuml.test.restlet.Human_ring_ServerResourceImpl;
import org.tuml.test.restlet.HumansServerResourceImpl;
import org.tuml.test.restlet.RingServerResourceImpl;
import org.tuml.test.restlet.Ring_finger_ServerResourceImpl;
import org.tuml.test.restlet.Ring_human_ServerResourceImpl;

public enum RestletRouterEnum {
	HAND("/hands/{handId}",HandServerResourceImpl.class),
	FINGER("/fingers/{fingerId}",FingerServerResourceImpl.class),
	RING("/rings/{ringId}",RingServerResourceImpl.class),
	HUMAN("/humans/{humanId}",HumanServerResourceImpl.class),
	HAND_finger("/hands/{handId}/finger",Hand_finger_ServerResourceImpl.class),
	HUMAN_hand("/humans/{humanId}/hand",Human_hand_ServerResourceImpl.class),
	HUMAN_ring("/humans/{humanId}/ring",Human_ring_ServerResourceImpl.class),
	HAND_human("/hands/{handId}/human",Hand_human_ServerResourceImpl.class),
	RING_human("/rings/{ringId}/human",Ring_human_ServerResourceImpl.class),
	FINGER_hand("/fingers/{fingerId}/hand",Finger_hand_ServerResourceImpl.class),
	FINGER_ring("/fingers/{fingerId}/ring",Finger_ring_ServerResourceImpl.class),
	RING_finger("/rings/{ringId}/finger",Ring_finger_ServerResourceImpl.class),
	HUMANS("/humans",HumansServerResourceImpl.class),
	ROOT("/",RootServerResourceImpl.class);
	private String uri;
	private Class<? extends ServerResource> serverResource;
	/**
	 * constructor for RestletRouterEnum
	 * 
	 * @param uri 
	 * @param serverResource 
	 */
	private RestletRouterEnum(String uri, Class<? extends ServerResource> serverResource) {
		this.uri = uri;
		this.serverResource = serverResource;
	}

	public void attach(Router router) {
		router.attach(uri, serverResource);
	}
	
	static public void attachAll(Router router) {
		RestletRouterEnum.HAND.attach(router);
		RestletRouterEnum.FINGER.attach(router);
		RestletRouterEnum.RING.attach(router);
		RestletRouterEnum.HUMAN.attach(router);
		RestletRouterEnum.HAND_finger.attach(router);
		RestletRouterEnum.HUMAN_hand.attach(router);
		RestletRouterEnum.HUMAN_ring.attach(router);
		RestletRouterEnum.HAND_human.attach(router);
		RestletRouterEnum.RING_human.attach(router);
		RestletRouterEnum.FINGER_hand.attach(router);
		RestletRouterEnum.FINGER_ring.attach(router);
		RestletRouterEnum.RING_finger.attach(router);
		RestletRouterEnum.HUMANS.attach(router);
		RestletRouterEnum.ROOT.attach(router);
	}
	
	public Class<? extends ServerResource> getServerResource() {
		return this.serverResource;
	}
	
	public String getUri() {
		return this.uri;
	}

}