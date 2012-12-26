package org.test.restlet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.tuml.framework.ModelLoader;
import org.tuml.ocl.TumlOcl2Parser;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.NakedGraph;
import org.tuml.runtime.adaptor.NakedGraphFactory;
import org.tuml.test.*;

import com.tinkerpop.blueprints.TransactionalGraph.Conclusion;
import org.tuml.test.meta.HumanMeta;

public class TumlRestletServerComponent2 extends Component {
	private static final Logger logger = Logger.getLogger(TumlRestletServerComponent2.class.getPackage().getName());
	public static void main(String[] args) throws Exception {
		new TumlRestletServerComponent2().start();
	}

	public TumlRestletServerComponent2() {
		//Load the model async
		new Thread(new Runnable() {
			@Override
			public void run() {
				ModelLoader.loadModel(new File("./test/test-restlet/src/main/model/restANDjson.uml"));
				@SuppressWarnings("unused")
				TumlOcl2Parser instance = TumlOcl2Parser.INSTANCE;
			}
		}).start();
		
		logger.info("start creating graph");
		GraphDb.setDb(createNakedGraph());
		logger.info("done creating graph");
		logger.info("start creating default data");
		createDefaultData();

		logger.info("done creating default data");
//		 create1000000();

		// Set basic properties
		setName("AAAAAAAAAAAA");
		setDescription("XXXXXXXXXXXX");
		setOwner("BBBBBBBBBBBB");
		setAuthor("CCCCCCCCCC");

        getClients().add(Protocol.FILE);
        getClients().add(Protocol.CLAP);
//        getClients().add(Protocol.RIAP);

		// Add connectors
		Server server = new Server(new Context(), Protocol.HTTP, 8111);
		server.getContext().getParameters().set("tracing", "true");
		getServers().add(server);

		// Attach the application to the default virtual host
		getDefaultHost().attach("/restAndJson", new TumlRestletServerApplication2());
		getDefaultHost().attach("/tumllib", new TumlRestletServerApplication2());
	}

	private void createDefaultData() {
		for (int i = 0; i < 5; i++) {
			Human human = new Human(true);
			human.setName("human1" + i);
			human.setName2("human2" + i);
			human.setGender(Gender.MALE);
			Home home = new Home(human);
			home.setName("home" + i);
            ComponentMany componentMany = new ComponentMany(human);
            componentMany.setName("componentMany" + i);

            ComponentManyDeep1 componentManyDeep1 = new ComponentManyDeep1(human);
            componentManyDeep1.setName("componentManyDeep1" + i);

            ComponentManyDeep2 componentManyDeep2 = new ComponentManyDeep2(componentManyDeep1);
            componentManyDeep2.setName("componentManyDeep2" + i);

            ComponentOneDeep3 componentOneDeep3 = new ComponentOneDeep3(componentManyDeep2);
            componentOneDeep3.setName("componentOneDeep3" + i);

			for (int j = 0; j < 10; j++) {
				Many1 many1 = new Many1(human);
				many1.setName("many1" + j);
				Many2 many2 = new Many2(human);
				many2.setName("many2" + j);
				One one = new One(human);
				one.setName("one" + j);
				many1.addToMany2(many2);
				if (j % 2 == 1) {
					many1.addToOne(one);
				}
			}

			for (int j = 0; j < 2; j++) {
				Hand hand = new Hand(human);
				hand.setName("hand" + j);
				hand.setTestNumber(50);
				hand.setTestBoolean(false);
				hand.setTestUnlimitedNatural(1 + j * 10000000L);

                AnotherOne1 anotherOne1 = new AnotherOne1(hand);
                anotherOne1.setName("anotherOne1" + j);

                AnotherOne2 anotherOne2 = new AnotherOne2(anotherOne1);
                anotherOne2.setName("anotherOne2" + j);

                AnotherMany1 anotherMany1 = new AnotherMany1(anotherOne2);
                anotherMany1.setName("anotherMany1" + j);

				for (int k = 0; k < 5; k++) {
					Finger finger = new Finger(hand);
					finger.setName("finger" + k);
					finger.addToManyInteger(1);
					finger.addToManyInteger(2);
					finger.addToManyInteger(3);
					
					finger.addToManyRequiredInteger(6);
					
					Ring ring = new Ring(human);
					ring.setName("ring" + i + j + k);
					finger.setRing(ring);
				}
				Ring ring = new Ring(human);
				ring.setName("ringExtra" + i + j);
			}
		}

		for (int i = 0; i < 10; i++) {
			Alien alien = new Alien(true);
			alien.setName("alien" + i);
			for (int j = 0; j < 2; j++) {
				SpaceCraft spaceCraft = new SpaceCraft(alien);
				spaceCraft.setName("spaceCraftShip" + j);
				spaceCraft.setIntergalactic(true);

				TerrestrialCraft terrestrialCraft = new TerrestrialCraft(alien);
				terrestrialCraft.setName("terrestrialCraftShip" + j);
				terrestrialCraft.setAquatic(true);
			}
		}
        HumanMeta h = HumanMeta.INSTANCE;
		GraphDb.getDb().stopTransaction(Conclusion.SUCCESS);
	}

	private void create1000000() {
		GraphDb.getDb().startTransaction();
		for (int i = 0; i < 1; i++) {
			Human human = new Human(true);
			human.setName("human1" + i);
			human.setName2("human2" + i);

			for (int j = 0; j < 1; j++) {
				Hand hand = new Hand(human);
				hand.setName("hand" + j);
				hand.setTestNumber(i + 1);
				hand.setTestBoolean(false);
				hand.setTestUnlimitedNatural(1 + j * 10000000L);

				for (int k = 0; k < 100000; k++) {
					if (k % 1000 == 0) {
						System.out.println(k);
					}
					Finger finger = new Finger(hand);
					finger.setName("finger" + k);

					Ring ring = new Ring(human);
					ring.setName("ring" + i + j + k);
					finger.setRing(ring);
				}
				Ring ring = new Ring(human);
				ring.setName("ringExtra" + i + j);
			}
		}
		GraphDb.getDb().stopTransaction(Conclusion.SUCCESS);
	}

	protected NakedGraph createNakedGraph() {
		Properties properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("tuml.env.properties"));
		} catch (FileNotFoundException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		String dbUrl = properties.getProperty("tinkerdb");
		String parsedUrl = dbUrl;
		if (dbUrl.startsWith("local:")) {
			parsedUrl = dbUrl.replace("local:", "");
		}
		File dir = new File(parsedUrl);
		if (dir.exists()) {
			try {
				FileUtils.deleteDirectory(dir);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		try {
			@SuppressWarnings("unchecked")
			Class<NakedGraphFactory> factory = (Class<NakedGraphFactory>) Class.forName(properties.getProperty("nakedgraph.factory"));
			Method m = factory.getDeclaredMethod("getInstance", new Class[0]);
			NakedGraphFactory nakedGraphFactory = (NakedGraphFactory) m.invoke(null);
			// TinkerSchemaHelper schemaHelper = (TinkerSchemaHelper)
			// Class.forName(properties.getProperty("schema.generator")).newInstance();
			String dbWithSchemata = properties.getProperty("tinkerdb.withschema", "false");
			return nakedGraphFactory.getNakedGraph(dbUrl, null, new Boolean(dbWithSchemata));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
