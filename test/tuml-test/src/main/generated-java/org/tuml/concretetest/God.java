package org.tuml.concretetest;

import com.tinkerpop.blueprints.CloseableIterable;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.tuml.collectiontest.Dream;
import org.tuml.collectiontest.Dream.DreamRuntimePropertyEnum;
import org.tuml.collectiontest.Fantasy;
import org.tuml.collectiontest.Fantasy.FantasyRuntimePropertyEnum;
import org.tuml.collectiontest.Foot;
import org.tuml.collectiontest.Foot.FootRuntimePropertyEnum;
import org.tuml.collectiontest.Hand;
import org.tuml.collectiontest.Hand.HandRuntimePropertyEnum;
import org.tuml.collectiontest.Nightmare;
import org.tuml.collectiontest.Nightmare.NightmareRuntimePropertyEnum;
import org.tuml.collectiontest.World;
import org.tuml.collectiontest.World.WorldRuntimePropertyEnum;
import org.tuml.concretetest.Angel.AngelRuntimePropertyEnum;
import org.tuml.concretetest.Demon.DemonRuntimePropertyEnum;
import org.tuml.concretetest.Universe.UniverseRuntimePropertyEnum;
import org.tuml.embeddedtest.REASON;
import org.tuml.hierarchytest.FakeRootFolder;
import org.tuml.hierarchytest.FakeRootFolder.FakeRootFolderRuntimePropertyEnum;
import org.tuml.hierarchytest.RealRootFolder;
import org.tuml.hierarchytest.RealRootFolder.RealRootFolderRuntimePropertyEnum;
import org.tuml.inheritencetest.AbstractSpecies;
import org.tuml.inheritencetest.AbstractSpecies.AbstractSpeciesRuntimePropertyEnum;
import org.tuml.inheritencetest.Mamal;
import org.tuml.interfacetest.Being;
import org.tuml.interfacetest.Being.BeingRuntimePropertyEnum;
import org.tuml.interfacetest.IMany;
import org.tuml.interfacetest.IMany.IManyRuntimePropertyEnum;
import org.tuml.interfacetest.Spirit;
import org.tuml.interfacetest.Spirit.SpiritRuntimePropertyEnum;
import org.tuml.navigability.NonNavigableMany;
import org.tuml.navigability.NonNavigableMany.NonNavigableManyRuntimePropertyEnum;
import org.tuml.navigability.NonNavigableOne;
import org.tuml.navigability.NonNavigableOne.NonNavigableOneRuntimePropertyEnum;
import org.tuml.onetoone.OneOne;
import org.tuml.onetoone.OneOne.OneOneRuntimePropertyEnum;
import org.tuml.onetoone.OneTwo;
import org.tuml.onetoone.OneTwo.OneTwoRuntimePropertyEnum;
import org.tuml.qualifiertest.Many1;
import org.tuml.qualifiertest.Many1.Many1RuntimePropertyEnum;
import org.tuml.qualifiertest.Many2;
import org.tuml.qualifiertest.Many2.Many2RuntimePropertyEnum;
import org.tuml.qualifiertest.Nature;
import org.tuml.qualifiertest.Nature.NatureRuntimePropertyEnum;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.collection.Multiplicity;
import org.tuml.runtime.collection.Qualifier;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerQualifiedBag;
import org.tuml.runtime.collection.TinkerQualifiedOrderedSet;
import org.tuml.runtime.collection.TinkerQualifiedSequence;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.collection.memory.TumlMemorySet;
import org.tuml.runtime.collection.persistent.TinkerBagClosableIterableImpl;
import org.tuml.runtime.collection.persistent.TinkerBagImpl;
import org.tuml.runtime.collection.persistent.TinkerOrderedSetClosableIterableImpl;
import org.tuml.runtime.collection.persistent.TinkerOrderedSetImpl;
import org.tuml.runtime.collection.persistent.TinkerQualifiedBagImpl;
import org.tuml.runtime.collection.persistent.TinkerQualifiedOrderedSetImpl;
import org.tuml.runtime.collection.persistent.TinkerQualifiedSequenceImpl;
import org.tuml.runtime.collection.persistent.TinkerQualifiedSetImpl;
import org.tuml.runtime.collection.persistent.TinkerSequenceImpl;
import org.tuml.runtime.collection.persistent.TinkerSetClosableIterableImpl;
import org.tuml.runtime.collection.persistent.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTuml;
import org.tuml.runtime.domain.TumlNode;
import org.tuml.runtime.util.TumlCollections;

public class God extends BaseTuml implements TumlNode {
	static final public long serialVersionUID = 1L;
	private TinkerSet<String> name;
	private TinkerSet<Universe> universe;
	private TinkerSet<Angel> angel;
	private TinkerSet<Spirit> spirit;
	private TinkerSet<Being> being;
	private TinkerSet<AbstractSpecies> abstractSpecies;
	private TinkerSet<IMany> iMany;
	private TinkerSet<String> embeddedString;
	private TinkerSet<Integer> embeddedInteger;
	private TinkerSet<RealRootFolder> realRootFolder;
	private TinkerSet<FakeRootFolder> fakeRootFolder;
	private TinkerSet<REASON> reason;
	private TinkerSet<Mamal> pet;
	private TinkerSet<Mamal> animalFarm;
	private TinkerQualifiedSet<Nature> nature;
	private TinkerSequence<Hand> hand;
	private TinkerQualifiedSequence<Foot> foot;
	private TinkerOrderedSet<World> world;
	private TinkerQualifiedOrderedSet<Fantasy> fantasy;
	private TinkerSet<Many1> many1;
	private TinkerSet<Many2> many2;
	private TinkerBag<Dream> dream;
	private TinkerQualifiedBag<Nightmare> nightmare;
	private TinkerSet<Demon> demon;
	private TinkerSet<OneOne> oneOne;
	private TinkerSet<OneTwo> oneTwo;
	private TinkerSet<NonNavigableOne> nonNavigableOne;
	private TinkerSet<NonNavigableMany> nonNavigableMany;
	private TinkerSet<REASON> rEASON;
	private TinkerQualifiedBag<Nightmare> memory;

	/**
	 * constructor for God
	 * 
	 * @param vertex 
	 */
	public God(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/**
	 * default constructor for God
	 */
	public God() {
		setName("//TODO 'whatajol'");
	}
	
	/**
	 * constructor for God
	 * 
	 * @param persistent 
	 */
	public God(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		this.vertex.setProperty("className", getClass().getName());
		defaultCreate();
		initialiseProperties();
		initVariables();
		createComponents();
		Edge edge = GraphDb.getDb().addEdge(null, GraphDb.getDb().getRoot(), this.vertex, "root");
		edge.setProperty("inClass", this.getClass().getName());
	}

	public void addToAbstractSpecies(AbstractSpecies abstractSpecies) {
		if ( abstractSpecies != null ) {
			abstractSpecies.clearGod();
			abstractSpecies.initialiseProperty(AbstractSpeciesRuntimePropertyEnum.god);
		}
		if ( abstractSpecies != null ) {
			this.abstractSpecies.add(abstractSpecies);
		}
	}
	
	public void addToAbstractSpecies(TinkerSet<AbstractSpecies> abstractSpecies) {
		if ( !abstractSpecies.isEmpty() ) {
			this.abstractSpecies.addAll(abstractSpecies);
		}
	}
	
	public void addToAngel(Angel angel) {
		if ( angel != null ) {
			angel.clearGod();
			angel.initialiseProperty(AngelRuntimePropertyEnum.god);
		}
		if ( angel != null ) {
			this.angel.add(angel);
		}
	}
	
	public void addToAngel(TinkerSet<Angel> angel) {
		if ( !angel.isEmpty() ) {
			this.angel.addAll(angel);
		}
	}
	
	public void addToAnimalFarm(Mamal animalFarm) {
		if ( animalFarm != null ) {
			this.animalFarm.add(animalFarm);
		}
	}
	
	public void addToAnimalFarm(TinkerSet<Mamal> animalFarm) {
		if ( !animalFarm.isEmpty() ) {
			this.animalFarm.addAll(animalFarm);
		}
	}
	
	public void addToBeing(Being being) {
		if ( being != null ) {
			being.clearGod();
			being.initialiseProperty(BeingRuntimePropertyEnum.god);
		}
		if ( being != null ) {
			this.being.add(being);
		}
	}
	
	public void addToBeing(TinkerSet<Being> being) {
		if ( !being.isEmpty() ) {
			this.being.addAll(being);
		}
	}
	
	public void addToDemon(Demon demon) {
		if ( demon != null ) {
			demon.clearGod();
			demon.initialiseProperty(DemonRuntimePropertyEnum.god);
		}
		if ( demon != null ) {
			this.demon.add(demon);
		}
	}
	
	public void addToDemon(TinkerSet<Demon> demon) {
		if ( !demon.isEmpty() ) {
			this.demon.addAll(demon);
		}
	}
	
	public void addToDream(Dream dream) {
		if ( dream != null ) {
			dream.clearGod();
			dream.initialiseProperty(DreamRuntimePropertyEnum.god);
		}
		if ( dream != null ) {
			this.dream.add(dream);
		}
	}
	
	public void addToDream(TinkerBag<Dream> dream) {
		if ( !dream.isEmpty() ) {
			this.dream.addAll(dream);
		}
	}
	
	public void addToEmbeddedInteger(Integer embeddedInteger) {
		if ( embeddedInteger != null ) {
			this.embeddedInteger.add(embeddedInteger);
		}
	}
	
	public void addToEmbeddedInteger(TinkerSet<Integer> embeddedInteger) {
		if ( !embeddedInteger.isEmpty() ) {
			this.embeddedInteger.addAll(embeddedInteger);
		}
	}
	
	public void addToEmbeddedString(String embeddedString) {
		if ( embeddedString != null ) {
			this.embeddedString.add(embeddedString);
		}
	}
	
	public void addToEmbeddedString(TinkerSet<String> embeddedString) {
		if ( !embeddedString.isEmpty() ) {
			this.embeddedString.addAll(embeddedString);
		}
	}
	
	public void addToFakeRootFolder(FakeRootFolder fakeRootFolder) {
		if ( fakeRootFolder != null ) {
			fakeRootFolder.clearGod();
			fakeRootFolder.initialiseProperty(FakeRootFolderRuntimePropertyEnum.god);
		}
		if ( fakeRootFolder != null ) {
			this.fakeRootFolder.add(fakeRootFolder);
		}
	}
	
	public void addToFakeRootFolder(TinkerSet<FakeRootFolder> fakeRootFolder) {
		if ( !fakeRootFolder.isEmpty() ) {
			this.fakeRootFolder.addAll(fakeRootFolder);
		}
	}
	
	public void addToFantasy(Fantasy fantasy) {
		if ( fantasy != null ) {
			fantasy.clearGod();
			fantasy.initialiseProperty(FantasyRuntimePropertyEnum.god);
		}
		if ( fantasy != null ) {
			this.fantasy.add(fantasy);
		}
	}
	
	public void addToFantasy(TinkerOrderedSet<Fantasy> fantasy) {
		for ( Fantasy f : fantasy ) {
			this.addToFantasy(f);
		}
	}
	
	public void addToFoot(Foot foot) {
		if ( foot != null ) {
			foot.clearGod();
			foot.initialiseProperty(FootRuntimePropertyEnum.god);
		}
		if ( foot != null ) {
			this.foot.add(foot);
		}
	}
	
	public void addToFoot(TinkerSequence<Foot> foot) {
		for ( Foot f : foot ) {
			this.addToFoot(f);
		}
	}
	
	public void addToHand(Hand hand) {
		if ( hand != null ) {
			hand.clearGod();
			hand.initialiseProperty(HandRuntimePropertyEnum.god);
		}
		if ( hand != null ) {
			this.hand.add(hand);
		}
	}
	
	public void addToHand(TinkerSequence<Hand> hand) {
		if ( !hand.isEmpty() ) {
			this.hand.addAll(hand);
		}
	}
	
	public void addToIMany(IMany iMany) {
		if ( iMany != null ) {
			iMany.clearGod();
			iMany.initialiseProperty(IManyRuntimePropertyEnum.god);
		}
		if ( iMany != null ) {
			this.iMany.add(iMany);
		}
	}
	
	public void addToIMany(TinkerSet<IMany> iMany) {
		if ( !iMany.isEmpty() ) {
			this.iMany.addAll(iMany);
		}
	}
	
	public void addToMany1(Many1 many1) {
		if ( many1 != null ) {
			many1.clearGod();
			many1.initialiseProperty(Many1RuntimePropertyEnum.god);
		}
		if ( many1 != null ) {
			this.many1.add(many1);
		}
	}
	
	public void addToMany1(TinkerSet<Many1> many1) {
		if ( !many1.isEmpty() ) {
			this.many1.addAll(many1);
		}
	}
	
	public void addToMany2(Many2 many2) {
		if ( many2 != null ) {
			many2.clearGod();
			many2.initialiseProperty(Many2RuntimePropertyEnum.god);
		}
		if ( many2 != null ) {
			this.many2.add(many2);
		}
	}
	
	public void addToMany2(TinkerSet<Many2> many2) {
		if ( !many2.isEmpty() ) {
			this.many2.addAll(many2);
		}
	}
	
	public void addToMemory(Nightmare memory) {
		if ( memory != null ) {
			memory.clearGodOfMemory();
			memory.initialiseProperty(NightmareRuntimePropertyEnum.godOfMemory);
		}
		if ( memory != null ) {
			this.memory.add(memory);
		}
	}
	
	public void addToMemory(TinkerBag<Nightmare> memory) {
		for ( Nightmare m : memory ) {
			this.addToMemory(m);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToNature(Nature nature) {
		if ( nature != null ) {
			nature.clearGod();
			nature.initialiseProperty(NatureRuntimePropertyEnum.god);
		}
		if ( nature != null ) {
			this.nature.add(nature);
		}
	}
	
	public void addToNature(TinkerSet<Nature> nature) {
		for ( Nature n : nature ) {
			this.addToNature(n);
		}
	}
	
	public void addToNightmare(Nightmare nightmare) {
		if ( nightmare != null ) {
			nightmare.clearGod();
			nightmare.initialiseProperty(NightmareRuntimePropertyEnum.god);
		}
		if ( nightmare != null ) {
			this.nightmare.add(nightmare);
		}
	}
	
	public void addToNightmare(TinkerBag<Nightmare> nightmare) {
		for ( Nightmare n : nightmare ) {
			this.addToNightmare(n);
		}
	}
	
	public void addToNonNavigableMany(NonNavigableMany nonNavigableMany) {
		if ( nonNavigableMany != null ) {
			nonNavigableMany.clearGod();
			nonNavigableMany.initialiseProperty(NonNavigableManyRuntimePropertyEnum.god);
		}
		if ( nonNavigableMany != null ) {
			this.nonNavigableMany.add(nonNavigableMany);
		}
	}
	
	public void addToNonNavigableMany(TinkerSet<NonNavigableMany> nonNavigableMany) {
		if ( !nonNavigableMany.isEmpty() ) {
			this.nonNavigableMany.addAll(nonNavigableMany);
		}
	}
	
	public void addToNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			nonNavigableOne.clearGod();
			nonNavigableOne.initialiseProperty(NonNavigableOneRuntimePropertyEnum.god);
		}
		if ( nonNavigableOne != null ) {
			this.nonNavigableOne.add(nonNavigableOne);
		}
	}
	
	public void addToNonNavigableOne(TinkerSet<NonNavigableOne> nonNavigableOne) {
		if ( !nonNavigableOne.isEmpty() ) {
			this.nonNavigableOne.addAll(nonNavigableOne);
		}
	}
	
	public void addToOneOne(OneOne oneOne) {
		if ( oneOne != null ) {
			oneOne.clearGod();
			oneOne.initialiseProperty(OneOneRuntimePropertyEnum.god);
		}
		if ( oneOne != null ) {
			this.oneOne.add(oneOne);
		}
	}
	
	public void addToOneOne(TinkerSet<OneOne> oneOne) {
		if ( !oneOne.isEmpty() ) {
			this.oneOne.addAll(oneOne);
		}
	}
	
	public void addToOneTwo(OneTwo oneTwo) {
		if ( oneTwo != null ) {
			oneTwo.clearGod();
			oneTwo.initialiseProperty(OneTwoRuntimePropertyEnum.god);
		}
		if ( oneTwo != null ) {
			this.oneTwo.add(oneTwo);
		}
	}
	
	public void addToOneTwo(TinkerSet<OneTwo> oneTwo) {
		if ( !oneTwo.isEmpty() ) {
			this.oneTwo.addAll(oneTwo);
		}
	}
	
	public void addToPet(Mamal pet) {
		if ( pet != null ) {
			this.pet.add(pet);
		}
	}
	
	public void addToREASON(REASON rEASON) {
		if ( rEASON != null ) {
			this.rEASON.add(rEASON);
		}
	}
	
	public void addToREASON(TinkerSet<REASON> rEASON) {
		if ( !rEASON.isEmpty() ) {
			this.rEASON.addAll(rEASON);
		}
	}
	
	public void addToRealRootFolder(RealRootFolder realRootFolder) {
		if ( realRootFolder != null ) {
			realRootFolder.clearGod();
			realRootFolder.initialiseProperty(RealRootFolderRuntimePropertyEnum.god);
		}
		if ( realRootFolder != null ) {
			this.realRootFolder.add(realRootFolder);
		}
	}
	
	public void addToRealRootFolder(TinkerSet<RealRootFolder> realRootFolder) {
		if ( !realRootFolder.isEmpty() ) {
			this.realRootFolder.addAll(realRootFolder);
		}
	}
	
	public void addToReason(REASON reason) {
		if ( reason != null ) {
			this.reason.add(reason);
		}
	}
	
	public void addToSpirit(Spirit spirit) {
		if ( spirit != null ) {
			spirit.clearGod();
			spirit.initialiseProperty(SpiritRuntimePropertyEnum.god);
		}
		if ( spirit != null ) {
			this.spirit.add(spirit);
		}
	}
	
	public void addToSpirit(TinkerSet<Spirit> spirit) {
		if ( !spirit.isEmpty() ) {
			this.spirit.addAll(spirit);
		}
	}
	
	public void addToUniverse(TinkerSet<Universe> universe) {
		if ( !universe.isEmpty() ) {
			this.universe.addAll(universe);
		}
	}
	
	public void addToUniverse(Universe universe) {
		if ( universe != null ) {
			universe.clearGod();
			universe.initialiseProperty(UniverseRuntimePropertyEnum.god);
		}
		if ( universe != null ) {
			this.universe.add(universe);
		}
	}
	
	public void addToWorld(TinkerOrderedSet<World> world) {
		if ( !world.isEmpty() ) {
			this.world.addAll(world);
		}
	}
	
	public void addToWorld(World world) {
		if ( world != null ) {
			world.clearGod();
			world.initialiseProperty(WorldRuntimePropertyEnum.god);
		}
		if ( world != null ) {
			this.world.add(world);
		}
	}
	
	public void clearAbstractSpecies() {
		this.abstractSpecies.clear();
	}
	
	public void clearAngel() {
		this.angel.clear();
	}
	
	public void clearAnimalFarm() {
		this.animalFarm.clear();
	}
	
	public void clearBeing() {
		this.being.clear();
	}
	
	public void clearDemon() {
		this.demon.clear();
	}
	
	public void clearDream() {
		this.dream.clear();
	}
	
	public void clearEmbeddedInteger() {
		this.embeddedInteger.clear();
	}
	
	public void clearEmbeddedString() {
		this.embeddedString.clear();
	}
	
	public void clearFakeRootFolder() {
		this.fakeRootFolder.clear();
	}
	
	public void clearFantasy() {
		this.fantasy.clear();
	}
	
	public void clearFoot() {
		this.foot.clear();
	}
	
	public void clearHand() {
		this.hand.clear();
	}
	
	public void clearIMany() {
		this.iMany.clear();
	}
	
	public void clearMany1() {
		this.many1.clear();
	}
	
	public void clearMany2() {
		this.many2.clear();
	}
	
	public void clearMemory() {
		this.memory.clear();
	}
	
	public void clearName() {
		this.name.clear();
	}
	
	public void clearNature() {
		this.nature.clear();
	}
	
	public void clearNightmare() {
		this.nightmare.clear();
	}
	
	public void clearNonNavigableMany() {
		this.nonNavigableMany.clear();
	}
	
	public void clearNonNavigableOne() {
		this.nonNavigableOne.clear();
	}
	
	public void clearOneOne() {
		this.oneOne.clear();
	}
	
	public void clearOneTwo() {
		this.oneTwo.clear();
	}
	
	public void clearPet() {
		this.pet.clear();
	}
	
	public void clearREASON() {
		this.rEASON.clear();
	}
	
	public void clearRealRootFolder() {
		this.realRootFolder.clear();
	}
	
	public void clearReason() {
		this.reason.clear();
	}
	
	public void clearSpirit() {
		this.spirit.clear();
	}
	
	public void clearUniverse() {
		this.universe.clear();
	}
	
	public void clearWorld() {
		this.world.clear();
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
		for ( World child : getWorld() ) {
			child.delete();
		}
		for ( OneOne child : getOneOne() ) {
			child.delete();
		}
		for ( Foot child : getFoot() ) {
			child.delete();
		}
		for ( Universe child : getUniverse() ) {
			child.delete();
		}
		for ( Being child : getBeing() ) {
			child.delete();
		}
		for ( NonNavigableOne child : getNonNavigableOne() ) {
			child.delete();
		}
		for ( OneTwo child : getOneTwo() ) {
			child.delete();
		}
		for ( Many2 child : getMany2() ) {
			child.delete();
		}
		for ( Hand child : getHand() ) {
			child.delete();
		}
		for ( AbstractSpecies child : getAbstractSpecies() ) {
			child.delete();
		}
		for ( Many1 child : getMany1() ) {
			child.delete();
		}
		for ( IMany child : getIMany() ) {
			child.delete();
		}
		for ( Angel child : getAngel() ) {
			child.delete();
		}
		for ( RealRootFolder child : getRealRootFolder() ) {
			child.delete();
		}
		for ( Nightmare child : getNightmare() ) {
			child.delete();
		}
		for ( Dream child : getDream() ) {
			child.delete();
		}
		for ( Demon child : getDemon() ) {
			child.delete();
		}
		for ( NonNavigableMany child : getNonNavigableMany() ) {
			child.delete();
		}
		for ( Fantasy child : getFantasy() ) {
			child.delete();
		}
		for ( FakeRootFolder child : getFakeRootFolder() ) {
			child.delete();
		}
		for ( Spirit child : getSpirit() ) {
			child.delete();
		}
		for ( Nature child : getNature() ) {
			child.delete();
		}
		if ( getPet() != null ) {
			getPet().delete();
		}
		for ( Mamal child : getAnimalFarm() ) {
			child.delete();
		}
		this.memory.clear();
		this.pet.clear();
		this.animalFarm.clear();
		GraphDb.getDb().removeVertex(this.vertex);
	}
	
	@Override
	public void fromJson(Map<String,Object> propertyMap) {
		for ( String propertyName : propertyMap.keySet() ) {
			if ( propertyName.equals("embeddedString") ) {
				@SuppressWarnings(	"unchecked")
				 TinkerSet<String> embeddedString = new TumlMemorySet<String>((Collection<String>)propertyMap.get(propertyName));
				setEmbeddedString(embeddedString);
			} else if ( propertyName.equals("reason") ) {
				setReason(REASON.fromJson((String)propertyMap.get(propertyName)));
			} else if ( propertyName.equals("name") ) {
				setName((String)propertyMap.get(propertyName));
			} else if ( propertyName.equals("embeddedInteger") ) {
				@SuppressWarnings(	"unchecked")
				 TinkerSet<Integer> embeddedInteger = new TumlMemorySet<Integer>((Collection<Integer>)propertyMap.get(propertyName));
				setEmbeddedInteger(embeddedInteger);
			} else if ( propertyName.equals("id") ) {
				//Ignored;
			} else {
				throw new IllegalStateException();
			}
		}
	}
	
	@Override
	public void fromJson(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings(	"unchecked")
			 Map<String,Object> propertyMap = mapper.readValue(json, Map.class);
			fromJson(propertyMap);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public TinkerSet<AbstractSpecies> getAbstractSpecies() {
		return this.abstractSpecies;
	}
	
	public TinkerSet<Angel> getAngel() {
		return this.angel;
	}
	
	public TinkerSet<Mamal> getAnimalFarm() {
		return this.animalFarm;
	}
	
	public TinkerSet<Being> getBeing() {
		return this.being;
	}
	
	public TinkerSet<Demon> getDemon() {
		return this.demon;
	}
	
	public TinkerBag<Dream> getDream() {
		return this.dream;
	}
	
	public TinkerSet<Integer> getEmbeddedInteger() {
		return this.embeddedInteger;
	}
	
	public TinkerSet<String> getEmbeddedString() {
		return this.embeddedString;
	}
	
	public TinkerSet<FakeRootFolder> getFakeRootFolder() {
		return this.fakeRootFolder;
	}
	
	public TinkerQualifiedOrderedSet<Fantasy> getFantasy() {
		return this.fantasy;
	}
	
	public TinkerOrderedSet<Fantasy> getFantasyForFantasyQualifierOnName(String fantasyQualifierOnName) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + GodRuntimePropertyEnum.fantasy.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "fantasyQualifierOnName";
			String indexValue = fantasyQualifierOnName==null?"___NULL___":fantasyQualifierOnName;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new TinkerOrderedSetClosableIterableImpl<Fantasy>(iterator, GodRuntimePropertyEnum.fantasy);
			} else {
				return TumlCollections.emptyOrderedSet();
			}
		}
	}
	
	public TinkerQualifiedSequence<Foot> getFoot() {
		return this.foot;
	}
	
	public Foot getFootForGodFootQualifier(String godFootQualifier) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + GodRuntimePropertyEnum.foot.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "godFootQualifier";
			String indexValue = godFootQualifier==null?"___NULL___":godFootQualifier;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new Foot(iterator.next().getVertex(Direction.IN));
			} else {
				return null;
			}
		}
	}
	
	public TinkerSequence<Hand> getHand() {
		return this.hand;
	}
	
	public TinkerSet<IMany> getIMany() {
		return this.iMany;
	}
	
	@Override
	public Long getId() {
		return TinkerIdUtilFactory.getIdUtil().getId(this.vertex);
	}
	
	public TinkerSet<Many1> getMany1() {
		return this.many1;
	}
	
	public TinkerSet<Many2> getMany2() {
		return this.many2;
	}
	
	public TinkerQualifiedBag<Nightmare> getMemory() {
		return this.memory;
	}
	
	public TinkerBag<Nightmare> getMemoryForMemoryQualifier1(String memoryQualifier1) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + GodRuntimePropertyEnum.memory.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "memoryQualifier1";
			String indexValue = memoryQualifier1==null?"___NULL___":memoryQualifier1;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new TinkerBagClosableIterableImpl<Nightmare>(iterator, GodRuntimePropertyEnum.memory);
			} else {
				return TumlCollections.emptyBag();
			}
		}
	}
	
	public String getName() {
		TinkerSet<String> tmp = this.name;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public TinkerQualifiedSet<Nature> getNature() {
		return this.nature;
	}
	
	public TinkerSet<Nature> getNatureForQualifier2(String qualifier2) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + GodRuntimePropertyEnum.nature.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "qualifier2";
			String indexValue = qualifier2==null?"___NULL___":qualifier2;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new TinkerSetClosableIterableImpl<Nature>(iterator, GodRuntimePropertyEnum.nature);
			} else {
				return TumlCollections.emptySet();
			}
		}
	}
	
	public TinkerQualifiedBag<Nightmare> getNightmare() {
		return this.nightmare;
	}
	
	public Nightmare getNightmareForQualifier1(String qualifier1) {
		Index<Edge> index = GraphDb.getDb().getIndex(getUid() + ":::" + GodRuntimePropertyEnum.nightmare.getLabel(), Edge.class);
		if ( index==null ) {
			return null;
		} else {
			String indexKey = "qualifier1";
			String indexValue = qualifier1==null?"___NULL___":qualifier1;
			CloseableIterable<Edge> closeableIterable = index.get(indexKey, indexValue);
			Iterator<Edge> iterator = closeableIterable.iterator();
			if ( iterator.hasNext() ) {
				return new Nightmare(iterator.next().getVertex(Direction.IN));
			} else {
				return null;
			}
		}
	}
	
	public TinkerSet<NonNavigableMany> getNonNavigableMany() {
		return this.nonNavigableMany;
	}
	
	public TinkerSet<NonNavigableOne> getNonNavigableOne() {
		return this.nonNavigableOne;
	}
	
	@Override
	public int getObjectVersion() {
		return TinkerIdUtilFactory.getIdUtil().getVersion(this.vertex);
	}
	
	public TinkerSet<OneOne> getOneOne() {
		return this.oneOne;
	}
	
	public TinkerSet<OneTwo> getOneTwo() {
		return this.oneTwo;
	}
	
	public Mamal getPet() {
		TinkerSet<Mamal> tmp = this.pet;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	public List<Qualifier> getQualifierForFantasy(Fantasy context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"fantasyQualifierOnName"}, new String[]{context.getFantasyQualifierOnName() == null ? "___NULL___" : context.getFantasyQualifierOnName().toString() }, Multiplicity.ZERO_TO_MANY));
		return result;
	}
	
	public List<Qualifier> getQualifierForFoot(Foot context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"godFootQualifier"}, new String[]{context.getGodFootQualifier() == null ? "___NULL___" : context.getGodFootQualifier().toString() }, Multiplicity.ZERO_TO_ONE));
		return result;
	}
	
	public List<Qualifier> getQualifierForMemory(Nightmare context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"memoryQualifier1"}, new String[]{context.getMemoryQualifier1() == null ? "___NULL___" : context.getMemoryQualifier1().toString() }, Multiplicity.ZERO_TO_MANY));
		return result;
	}
	
	public List<Qualifier> getQualifierForNature(Nature context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"qualifier2"}, new String[]{context.getQualifier2() == null ? "___NULL___" : context.getQualifier2().toString() }, Multiplicity.ZERO_TO_MANY));
		return result;
	}
	
	public List<Qualifier> getQualifierForNightmare(Nightmare context) {
		List<Qualifier> result = new ArrayList<Qualifier>();
		result.add(new Qualifier(new String[]{"qualifier1"}, new String[]{context.getQualifier1() == null ? "___NULL___" : context.getQualifier1().toString() }, Multiplicity.ZERO_TO_ONE));
		return result;
	}
	
	/**
	 * getQualifiers is called from the collection in order to update the index used to implement the qualifier
	 * 
	 * @param tumlRuntimeProperty 
	 * @param node 
	 */
	@Override
	public List<Qualifier> getQualifiers(TumlRuntimeProperty tumlRuntimeProperty, TumlNode node) {
		List<Qualifier> result = Collections.emptyList();
		GodRuntimePropertyEnum runtimeProperty = GodRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result.isEmpty() ) {
			switch ( runtimeProperty ) {
				case nature:
					result = getQualifierForNature((Nature)node);
				break;
			
				case fantasy:
					result = getQualifierForFantasy((Fantasy)node);
				break;
			
				case nightmare:
					result = getQualifierForNightmare((Nightmare)node);
				break;
			
				case foot:
					result = getQualifierForFoot((Foot)node);
				break;
			
				case memory:
					result = getQualifierForMemory((Nightmare)node);
				break;
			
				default:
					result = Collections.emptyList();
				break;
			}
		
		}
		return result;
	}
	
	public TinkerSet<REASON> getREASON() {
		return this.rEASON;
	}
	
	public TinkerSet<RealRootFolder> getRealRootFolder() {
		return this.realRootFolder;
	}
	
	public REASON getReason() {
		TinkerSet<REASON> tmp = this.reason;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
	}
	
	/**
	 * getSize is called from the collection in order to update the index used to implement a sequence's index
	 * 
	 * @param tumlRuntimeProperty 
	 */
	@Override
	public int getSize(TumlRuntimeProperty tumlRuntimeProperty) {
		int result = 0;
		GodRuntimePropertyEnum runtimeProperty = GodRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel());
		if ( runtimeProperty != null && result == 0 ) {
			switch ( runtimeProperty ) {
				case animalFarm:
					result = animalFarm.size();
				break;
			
				case nature:
					result = nature.size();
				break;
			
				case rEASON:
					result = rEASON.size();
				break;
			
				case nonNavigableMany:
					result = nonNavigableMany.size();
				break;
			
				case dream:
					result = dream.size();
				break;
			
				case fantasy:
					result = fantasy.size();
				break;
			
				case reason:
					result = reason.size();
				break;
			
				case demon:
					result = demon.size();
				break;
			
				case nightmare:
					result = nightmare.size();
				break;
			
				case angel:
					result = angel.size();
				break;
			
				case iMany:
					result = iMany.size();
				break;
			
				case name:
					result = name.size();
				break;
			
				case hand:
					result = hand.size();
				break;
			
				case many2:
					result = many2.size();
				break;
			
				case oneTwo:
					result = oneTwo.size();
				break;
			
				case foot:
					result = foot.size();
				break;
			
				case memory:
					result = memory.size();
				break;
			
				case embeddedString:
					result = embeddedString.size();
				break;
			
				case fakeRootFolder:
					result = fakeRootFolder.size();
				break;
			
				case spirit:
					result = spirit.size();
				break;
			
				case pet:
					result = pet.size();
				break;
			
				case world:
					result = world.size();
				break;
			
				case oneOne:
					result = oneOne.size();
				break;
			
				case nonNavigableOne:
					result = nonNavigableOne.size();
				break;
			
				case being:
					result = being.size();
				break;
			
				case universe:
					result = universe.size();
				break;
			
				case many1:
					result = many1.size();
				break;
			
				case abstractSpecies:
					result = abstractSpecies.size();
				break;
			
				case realRootFolder:
					result = realRootFolder.size();
				break;
			
				case embeddedInteger:
					result = embeddedInteger.size();
				break;
			
				default:
					result = 0;
				break;
			}
		
		}
		return result;
	}
	
	public TinkerSet<Spirit> getSpirit() {
		return this.spirit;
	}
	
	@Override
	public String getUid() {
		String uid = (String) this.vertex.getProperty("uid");
		if ( uid==null || uid.trim().length()==0 ) {
			uid=UUID.randomUUID().toString();
			this.vertex.setProperty("uid", uid);
		}
		return uid;
	}
	
	public TinkerSet<Universe> getUniverse() {
		return this.universe;
	}
	
	public TinkerOrderedSet<World> getWorld() {
		return this.world;
	}
	
	/**
	 * Implements the ocl statement for initialization variable 'name'
	 * <pre>
	 * package tumltest::org::tuml::concretetest
	 *     context God::name : String
	 *     derive: 'whatajol'
	 * endpackage
	 * </pre>
	 */
	public void initVariables() {
		setName("//TODO 'whatajol'");
	}
	
	@Override
	public void initialiseProperties() {
		this.embeddedString =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.embeddedString);
		this.memory =  new TinkerQualifiedBagImpl<Nightmare>(this, GodRuntimePropertyEnum.memory);
		this.foot =  new TinkerQualifiedSequenceImpl<Foot>(this, GodRuntimePropertyEnum.foot);
		this.oneTwo =  new TinkerSetImpl<OneTwo>(this, GodRuntimePropertyEnum.oneTwo);
		this.many2 =  new TinkerSetImpl<Many2>(this, GodRuntimePropertyEnum.many2);
		this.hand =  new TinkerSequenceImpl<Hand>(this, GodRuntimePropertyEnum.hand);
		this.name =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.name);
		this.iMany =  new TinkerSetImpl<IMany>(this, GodRuntimePropertyEnum.iMany);
		this.angel =  new TinkerSetImpl<Angel>(this, GodRuntimePropertyEnum.angel);
		this.nightmare =  new TinkerQualifiedBagImpl<Nightmare>(this, GodRuntimePropertyEnum.nightmare);
		this.demon =  new TinkerSetImpl<Demon>(this, GodRuntimePropertyEnum.demon);
		this.reason =  new TinkerSetImpl<REASON>(this, GodRuntimePropertyEnum.reason);
		this.fantasy =  new TinkerQualifiedOrderedSetImpl<Fantasy>(this, GodRuntimePropertyEnum.fantasy);
		this.fakeRootFolder =  new TinkerSetImpl<FakeRootFolder>(this, GodRuntimePropertyEnum.fakeRootFolder);
		this.spirit =  new TinkerSetImpl<Spirit>(this, GodRuntimePropertyEnum.spirit);
		this.pet =  new TinkerSetImpl<Mamal>(this, GodRuntimePropertyEnum.pet);
		this.world =  new TinkerOrderedSetImpl<World>(this, GodRuntimePropertyEnum.world);
		this.oneOne =  new TinkerSetImpl<OneOne>(this, GodRuntimePropertyEnum.oneOne);
		this.nonNavigableOne =  new TinkerSetImpl<NonNavigableOne>(this, GodRuntimePropertyEnum.nonNavigableOne);
		this.being =  new TinkerSetImpl<Being>(this, GodRuntimePropertyEnum.being);
		this.universe =  new TinkerSetImpl<Universe>(this, GodRuntimePropertyEnum.universe);
		this.many1 =  new TinkerSetImpl<Many1>(this, GodRuntimePropertyEnum.many1);
		this.abstractSpecies =  new TinkerSetImpl<AbstractSpecies>(this, GodRuntimePropertyEnum.abstractSpecies);
		this.realRootFolder =  new TinkerSetImpl<RealRootFolder>(this, GodRuntimePropertyEnum.realRootFolder);
		this.embeddedInteger =  new TinkerSetImpl<Integer>(this, GodRuntimePropertyEnum.embeddedInteger);
		this.dream =  new TinkerBagImpl<Dream>(this, GodRuntimePropertyEnum.dream);
		this.nonNavigableMany =  new TinkerSetImpl<NonNavigableMany>(this, GodRuntimePropertyEnum.nonNavigableMany);
		this.rEASON =  new TinkerSetImpl<REASON>(this, GodRuntimePropertyEnum.rEASON);
		this.nature =  new TinkerQualifiedSetImpl<Nature>(this, GodRuntimePropertyEnum.nature);
		this.animalFarm =  new TinkerSetImpl<Mamal>(this, GodRuntimePropertyEnum.animalFarm);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (GodRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case animalFarm:
				this.animalFarm =  new TinkerSetImpl<Mamal>(this, GodRuntimePropertyEnum.animalFarm);
			break;
		
			case nature:
				this.nature =  new TinkerQualifiedSetImpl<Nature>(this, GodRuntimePropertyEnum.nature);
			break;
		
			case rEASON:
				this.rEASON =  new TinkerSetImpl<REASON>(this, GodRuntimePropertyEnum.rEASON);
			break;
		
			case nonNavigableMany:
				this.nonNavigableMany =  new TinkerSetImpl<NonNavigableMany>(this, GodRuntimePropertyEnum.nonNavigableMany);
			break;
		
			case dream:
				this.dream =  new TinkerBagImpl<Dream>(this, GodRuntimePropertyEnum.dream);
			break;
		
			case fantasy:
				this.fantasy =  new TinkerQualifiedOrderedSetImpl<Fantasy>(this, GodRuntimePropertyEnum.fantasy);
			break;
		
			case reason:
				this.reason =  new TinkerSetImpl<REASON>(this, GodRuntimePropertyEnum.reason);
			break;
		
			case demon:
				this.demon =  new TinkerSetImpl<Demon>(this, GodRuntimePropertyEnum.demon);
			break;
		
			case nightmare:
				this.nightmare =  new TinkerQualifiedBagImpl<Nightmare>(this, GodRuntimePropertyEnum.nightmare);
			break;
		
			case angel:
				this.angel =  new TinkerSetImpl<Angel>(this, GodRuntimePropertyEnum.angel);
			break;
		
			case iMany:
				this.iMany =  new TinkerSetImpl<IMany>(this, GodRuntimePropertyEnum.iMany);
			break;
		
			case name:
				this.name =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.name);
			break;
		
			case hand:
				this.hand =  new TinkerSequenceImpl<Hand>(this, GodRuntimePropertyEnum.hand);
			break;
		
			case many2:
				this.many2 =  new TinkerSetImpl<Many2>(this, GodRuntimePropertyEnum.many2);
			break;
		
			case oneTwo:
				this.oneTwo =  new TinkerSetImpl<OneTwo>(this, GodRuntimePropertyEnum.oneTwo);
			break;
		
			case foot:
				this.foot =  new TinkerQualifiedSequenceImpl<Foot>(this, GodRuntimePropertyEnum.foot);
			break;
		
			case memory:
				this.memory =  new TinkerQualifiedBagImpl<Nightmare>(this, GodRuntimePropertyEnum.memory);
			break;
		
			case embeddedString:
				this.embeddedString =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.embeddedString);
			break;
		
			case fakeRootFolder:
				this.fakeRootFolder =  new TinkerSetImpl<FakeRootFolder>(this, GodRuntimePropertyEnum.fakeRootFolder);
			break;
		
			case spirit:
				this.spirit =  new TinkerSetImpl<Spirit>(this, GodRuntimePropertyEnum.spirit);
			break;
		
			case pet:
				this.pet =  new TinkerSetImpl<Mamal>(this, GodRuntimePropertyEnum.pet);
			break;
		
			case world:
				this.world =  new TinkerOrderedSetImpl<World>(this, GodRuntimePropertyEnum.world);
			break;
		
			case oneOne:
				this.oneOne =  new TinkerSetImpl<OneOne>(this, GodRuntimePropertyEnum.oneOne);
			break;
		
			case nonNavigableOne:
				this.nonNavigableOne =  new TinkerSetImpl<NonNavigableOne>(this, GodRuntimePropertyEnum.nonNavigableOne);
			break;
		
			case being:
				this.being =  new TinkerSetImpl<Being>(this, GodRuntimePropertyEnum.being);
			break;
		
			case universe:
				this.universe =  new TinkerSetImpl<Universe>(this, GodRuntimePropertyEnum.universe);
			break;
		
			case many1:
				this.many1 =  new TinkerSetImpl<Many1>(this, GodRuntimePropertyEnum.many1);
			break;
		
			case abstractSpecies:
				this.abstractSpecies =  new TinkerSetImpl<AbstractSpecies>(this, GodRuntimePropertyEnum.abstractSpecies);
			break;
		
			case realRootFolder:
				this.realRootFolder =  new TinkerSetImpl<RealRootFolder>(this, GodRuntimePropertyEnum.realRootFolder);
			break;
		
			case embeddedInteger:
				this.embeddedInteger =  new TinkerSetImpl<Integer>(this, GodRuntimePropertyEnum.embeddedInteger);
			break;
		
		}
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	public void removeFromAbstractSpecies(AbstractSpecies abstractSpecies) {
		if ( abstractSpecies != null ) {
			this.abstractSpecies.remove(abstractSpecies);
		}
	}
	
	public void removeFromAbstractSpecies(TinkerSet<AbstractSpecies> abstractSpecies) {
		if ( !abstractSpecies.isEmpty() ) {
			this.abstractSpecies.removeAll(abstractSpecies);
		}
	}
	
	public void removeFromAngel(Angel angel) {
		if ( angel != null ) {
			this.angel.remove(angel);
		}
	}
	
	public void removeFromAngel(TinkerSet<Angel> angel) {
		if ( !angel.isEmpty() ) {
			this.angel.removeAll(angel);
		}
	}
	
	public void removeFromAnimalFarm(Mamal animalFarm) {
		if ( animalFarm != null ) {
			this.animalFarm.remove(animalFarm);
		}
	}
	
	public void removeFromAnimalFarm(TinkerSet<Mamal> animalFarm) {
		if ( !animalFarm.isEmpty() ) {
			this.animalFarm.removeAll(animalFarm);
		}
	}
	
	public void removeFromBeing(Being being) {
		if ( being != null ) {
			this.being.remove(being);
		}
	}
	
	public void removeFromBeing(TinkerSet<Being> being) {
		if ( !being.isEmpty() ) {
			this.being.removeAll(being);
		}
	}
	
	public void removeFromDemon(Demon demon) {
		if ( demon != null ) {
			this.demon.remove(demon);
		}
	}
	
	public void removeFromDemon(TinkerSet<Demon> demon) {
		if ( !demon.isEmpty() ) {
			this.demon.removeAll(demon);
		}
	}
	
	public void removeFromDream(Dream dream) {
		if ( dream != null ) {
			this.dream.remove(dream);
		}
	}
	
	public void removeFromDream(TinkerBag<Dream> dream) {
		if ( !dream.isEmpty() ) {
			this.dream.removeAll(dream);
		}
	}
	
	public void removeFromEmbeddedInteger(Integer embeddedInteger) {
		if ( embeddedInteger != null ) {
			this.embeddedInteger.remove(embeddedInteger);
		}
	}
	
	public void removeFromEmbeddedInteger(TinkerSet<Integer> embeddedInteger) {
		if ( !embeddedInteger.isEmpty() ) {
			this.embeddedInteger.removeAll(embeddedInteger);
		}
	}
	
	public void removeFromEmbeddedString(String embeddedString) {
		if ( embeddedString != null ) {
			this.embeddedString.remove(embeddedString);
		}
	}
	
	public void removeFromEmbeddedString(TinkerSet<String> embeddedString) {
		if ( !embeddedString.isEmpty() ) {
			this.embeddedString.removeAll(embeddedString);
		}
	}
	
	public void removeFromFakeRootFolder(FakeRootFolder fakeRootFolder) {
		if ( fakeRootFolder != null ) {
			this.fakeRootFolder.remove(fakeRootFolder);
		}
	}
	
	public void removeFromFakeRootFolder(TinkerSet<FakeRootFolder> fakeRootFolder) {
		if ( !fakeRootFolder.isEmpty() ) {
			this.fakeRootFolder.removeAll(fakeRootFolder);
		}
	}
	
	public void removeFromFantasy(Fantasy fantasy) {
		if ( fantasy != null ) {
			this.fantasy.remove(fantasy);
		}
	}
	
	public void removeFromFantasy(TinkerOrderedSet<Fantasy> fantasy) {
		if ( !fantasy.isEmpty() ) {
			this.fantasy.removeAll(fantasy);
		}
	}
	
	public void removeFromFoot(Foot foot) {
		if ( foot != null ) {
			this.foot.remove(foot);
		}
	}
	
	public void removeFromFoot(TinkerSequence<Foot> foot) {
		if ( !foot.isEmpty() ) {
			this.foot.removeAll(foot);
		}
	}
	
	public void removeFromHand(Hand hand) {
		if ( hand != null ) {
			this.hand.remove(hand);
		}
	}
	
	public void removeFromHand(TinkerSequence<Hand> hand) {
		if ( !hand.isEmpty() ) {
			this.hand.removeAll(hand);
		}
	}
	
	public void removeFromIMany(IMany iMany) {
		if ( iMany != null ) {
			this.iMany.remove(iMany);
		}
	}
	
	public void removeFromIMany(TinkerSet<IMany> iMany) {
		if ( !iMany.isEmpty() ) {
			this.iMany.removeAll(iMany);
		}
	}
	
	public void removeFromMany1(Many1 many1) {
		if ( many1 != null ) {
			this.many1.remove(many1);
		}
	}
	
	public void removeFromMany1(TinkerSet<Many1> many1) {
		if ( !many1.isEmpty() ) {
			this.many1.removeAll(many1);
		}
	}
	
	public void removeFromMany2(Many2 many2) {
		if ( many2 != null ) {
			this.many2.remove(many2);
		}
	}
	
	public void removeFromMany2(TinkerSet<Many2> many2) {
		if ( !many2.isEmpty() ) {
			this.many2.removeAll(many2);
		}
	}
	
	public void removeFromMemory(Nightmare memory) {
		if ( memory != null ) {
			this.memory.remove(memory);
		}
	}
	
	public void removeFromMemory(TinkerBag<Nightmare> memory) {
		if ( !memory.isEmpty() ) {
			this.memory.removeAll(memory);
		}
	}
	
	public void removeFromName(String name) {
		if ( name != null ) {
			this.name.remove(name);
		}
	}
	
	public void removeFromName(TinkerSet<String> name) {
		if ( !name.isEmpty() ) {
			this.name.removeAll(name);
		}
	}
	
	public void removeFromNature(Nature nature) {
		if ( nature != null ) {
			this.nature.remove(nature);
		}
	}
	
	public void removeFromNature(TinkerSet<Nature> nature) {
		if ( !nature.isEmpty() ) {
			this.nature.removeAll(nature);
		}
	}
	
	public void removeFromNightmare(Nightmare nightmare) {
		if ( nightmare != null ) {
			this.nightmare.remove(nightmare);
		}
	}
	
	public void removeFromNightmare(TinkerBag<Nightmare> nightmare) {
		if ( !nightmare.isEmpty() ) {
			this.nightmare.removeAll(nightmare);
		}
	}
	
	public void removeFromNonNavigableMany(NonNavigableMany nonNavigableMany) {
		if ( nonNavigableMany != null ) {
			this.nonNavigableMany.remove(nonNavigableMany);
		}
	}
	
	public void removeFromNonNavigableMany(TinkerSet<NonNavigableMany> nonNavigableMany) {
		if ( !nonNavigableMany.isEmpty() ) {
			this.nonNavigableMany.removeAll(nonNavigableMany);
		}
	}
	
	public void removeFromNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			this.nonNavigableOne.remove(nonNavigableOne);
		}
	}
	
	public void removeFromNonNavigableOne(TinkerSet<NonNavigableOne> nonNavigableOne) {
		if ( !nonNavigableOne.isEmpty() ) {
			this.nonNavigableOne.removeAll(nonNavigableOne);
		}
	}
	
	public void removeFromOneOne(OneOne oneOne) {
		if ( oneOne != null ) {
			this.oneOne.remove(oneOne);
		}
	}
	
	public void removeFromOneOne(TinkerSet<OneOne> oneOne) {
		if ( !oneOne.isEmpty() ) {
			this.oneOne.removeAll(oneOne);
		}
	}
	
	public void removeFromOneTwo(OneTwo oneTwo) {
		if ( oneTwo != null ) {
			this.oneTwo.remove(oneTwo);
		}
	}
	
	public void removeFromOneTwo(TinkerSet<OneTwo> oneTwo) {
		if ( !oneTwo.isEmpty() ) {
			this.oneTwo.removeAll(oneTwo);
		}
	}
	
	public void removeFromPet(Mamal pet) {
		if ( pet != null ) {
			this.pet.remove(pet);
		}
	}
	
	public void removeFromPet(TinkerSet<Mamal> pet) {
		if ( !pet.isEmpty() ) {
			this.pet.removeAll(pet);
		}
	}
	
	public void removeFromREASON(REASON rEASON) {
		if ( rEASON != null ) {
			this.rEASON.remove(rEASON);
		}
	}
	
	public void removeFromREASON(TinkerSet<REASON> rEASON) {
		if ( !rEASON.isEmpty() ) {
			this.rEASON.removeAll(rEASON);
		}
	}
	
	public void removeFromRealRootFolder(RealRootFolder realRootFolder) {
		if ( realRootFolder != null ) {
			this.realRootFolder.remove(realRootFolder);
		}
	}
	
	public void removeFromRealRootFolder(TinkerSet<RealRootFolder> realRootFolder) {
		if ( !realRootFolder.isEmpty() ) {
			this.realRootFolder.removeAll(realRootFolder);
		}
	}
	
	public void removeFromReason(REASON reason) {
		if ( reason != null ) {
			this.reason.remove(reason);
		}
	}
	
	public void removeFromReason(TinkerSet<REASON> reason) {
		if ( !reason.isEmpty() ) {
			this.reason.removeAll(reason);
		}
	}
	
	public void removeFromSpirit(Spirit spirit) {
		if ( spirit != null ) {
			this.spirit.remove(spirit);
		}
	}
	
	public void removeFromSpirit(TinkerSet<Spirit> spirit) {
		if ( !spirit.isEmpty() ) {
			this.spirit.removeAll(spirit);
		}
	}
	
	public void removeFromUniverse(TinkerSet<Universe> universe) {
		if ( !universe.isEmpty() ) {
			this.universe.removeAll(universe);
		}
	}
	
	public void removeFromUniverse(Universe universe) {
		if ( universe != null ) {
			this.universe.remove(universe);
		}
	}
	
	public void removeFromWorld(TinkerOrderedSet<World> world) {
		if ( !world.isEmpty() ) {
			this.world.removeAll(world);
		}
	}
	
	public void removeFromWorld(World world) {
		if ( world != null ) {
			this.world.remove(world);
		}
	}
	
	public void setAbstractSpecies(TinkerSet<AbstractSpecies> abstractSpecies) {
		clearAbstractSpecies();
		addToAbstractSpecies(abstractSpecies);
	}
	
	public void setAngel(TinkerSet<Angel> angel) {
		clearAngel();
		addToAngel(angel);
	}
	
	public void setAnimalFarm(TinkerSet<Mamal> animalFarm) {
		clearAnimalFarm();
		addToAnimalFarm(animalFarm);
	}
	
	public void setBeing(TinkerSet<Being> being) {
		clearBeing();
		addToBeing(being);
	}
	
	public void setDemon(TinkerSet<Demon> demon) {
		clearDemon();
		addToDemon(demon);
	}
	
	public void setDream(TinkerBag<Dream> dream) {
		clearDream();
		addToDream(dream);
	}
	
	public void setEmbeddedInteger(TinkerSet<Integer> embeddedInteger) {
		clearEmbeddedInteger();
		addToEmbeddedInteger(embeddedInteger);
	}
	
	public void setEmbeddedString(TinkerSet<String> embeddedString) {
		clearEmbeddedString();
		addToEmbeddedString(embeddedString);
	}
	
	public void setFakeRootFolder(TinkerSet<FakeRootFolder> fakeRootFolder) {
		clearFakeRootFolder();
		addToFakeRootFolder(fakeRootFolder);
	}
	
	public void setFantasy(TinkerOrderedSet<Fantasy> fantasy) {
		clearFantasy();
		addToFantasy(fantasy);
	}
	
	public void setFoot(TinkerSequence<Foot> foot) {
		clearFoot();
		addToFoot(foot);
	}
	
	public void setHand(TinkerSequence<Hand> hand) {
		clearHand();
		addToHand(hand);
	}
	
	public void setIMany(TinkerSet<IMany> iMany) {
		clearIMany();
		addToIMany(iMany);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setMany1(TinkerSet<Many1> many1) {
		clearMany1();
		addToMany1(many1);
	}
	
	public void setMany2(TinkerSet<Many2> many2) {
		clearMany2();
		addToMany2(many2);
	}
	
	public void setMemory(TinkerBag<Nightmare> memory) {
		clearMemory();
		addToMemory(memory);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setNature(TinkerSet<Nature> nature) {
		clearNature();
		addToNature(nature);
	}
	
	public void setNightmare(TinkerBag<Nightmare> nightmare) {
		clearNightmare();
		addToNightmare(nightmare);
	}
	
	public void setNonNavigableMany(TinkerSet<NonNavigableMany> nonNavigableMany) {
		clearNonNavigableMany();
		addToNonNavigableMany(nonNavigableMany);
	}
	
	public void setNonNavigableOne(TinkerSet<NonNavigableOne> nonNavigableOne) {
		clearNonNavigableOne();
		addToNonNavigableOne(nonNavigableOne);
	}
	
	public void setOneOne(TinkerSet<OneOne> oneOne) {
		clearOneOne();
		addToOneOne(oneOne);
	}
	
	public void setOneTwo(TinkerSet<OneTwo> oneTwo) {
		clearOneTwo();
		addToOneTwo(oneTwo);
	}
	
	public void setPet(Mamal pet) {
		clearPet();
		addToPet(pet);
	}
	
	public void setREASON(TinkerSet<REASON> rEASON) {
		clearREASON();
		addToREASON(rEASON);
	}
	
	public void setRealRootFolder(TinkerSet<RealRootFolder> realRootFolder) {
		clearRealRootFolder();
		addToRealRootFolder(realRootFolder);
	}
	
	public void setReason(REASON reason) {
		clearReason();
		addToReason(reason);
	}
	
	public void setSpirit(TinkerSet<Spirit> spirit) {
		clearSpirit();
		addToSpirit(spirit);
	}
	
	public void setUniverse(TinkerSet<Universe> universe) {
		clearUniverse();
		addToUniverse(universe);
	}
	
	public void setWorld(TinkerOrderedSet<World> world) {
		clearWorld();
		addToWorld(world);
	}
	
	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\": " + getId() + ", ");
		sb.append("\"embeddedString\": " + getEmbeddedString().toJson() + "");
		sb.append(", ");
		sb.append("\"reason\": \"" + (getReason() == null ? "null" : getReason().toJson()) + "\"");
		sb.append(", ");
		sb.append("\"name\": \"" + getName() + "\"");
		sb.append(", ");
		sb.append("\"embeddedInteger\": " + getEmbeddedInteger().toJson() + "");
		sb.append("}");
		return sb.toString();
	}

	static public enum GodRuntimePropertyEnum implements TumlRuntimeProperty {
		embeddedString(false,true,true,false,"tumltest__org__tuml__concretetest__God__embeddedString",false,false,false,true,-1,0,false,false,false,false,true,"{\"embeddedString\": {\"onePrimitive\": false, \"manyPrimitive\": true, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": true, \"upper\": -1, \"lower\": 0, \"label\": \"tumltest__org__tuml__concretetest__God__embeddedString\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		memory(false,false,true,false,"A_<god>_<nightmare>_2",false,true,false,false,-1,0,true,false,false,false,false,"{\"memory\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<nightmare>_2\", \"qualified\": true, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": false}}"),
		foot(false,false,true,true,"A_<god>_<foot>",false,true,false,false,1,0,true,false,true,false,false,"{\"foot\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 0, \"label\": \"A_<god>_<foot>\", \"qualified\": true, \"inverseQualified\": false, \"inverseOrdered\": true, \"unique\": false}}"),
		oneTwo(false,false,true,true,"A_<god>_<oneTwo>",false,true,false,false,-1,0,false,false,false,false,true,"{\"oneTwo\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<oneTwo>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		many2(false,false,true,true,"A_<god>_<many2>",false,true,false,false,-1,0,false,false,false,false,true,"{\"many2\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<many2>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		hand(false,false,true,true,"A_<god>_<hand>",false,true,false,false,-1,0,false,false,true,false,false,"{\"hand\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<hand>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": true, \"unique\": false}}"),
		name(true,false,true,false,"tumltest__org__tuml__concretetest__God__name",false,false,true,false,1,1,false,false,false,false,true,"{\"name\": {\"onePrimitive\": true, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"tumltest__org__tuml__concretetest__God__name\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		iMany(false,false,true,true,"A_<god>_<iMany>",false,true,false,false,-1,0,false,false,false,false,true,"{\"iMany\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<iMany>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		angel(false,false,true,true,"A_<god>_<angel>",false,true,false,false,-1,0,false,false,false,false,true,"{\"angel\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<angel>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		nightmare(false,false,true,true,"A_<god>_<nightmare>",false,true,false,false,1,0,true,false,false,false,false,"{\"nightmare\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": 1, \"lower\": 0, \"label\": \"A_<god>_<nightmare>\", \"qualified\": true, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": false}}"),
		demon(false,false,true,true,"A_<god>_<demon>",false,true,false,false,-1,0,false,false,false,false,true,"{\"demon\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<demon>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		reason(false,false,true,false,"tumltest__org__tuml__concretetest__God__reason",false,false,true,false,1,1,false,false,false,false,true,"{\"reason\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"tumltest__org__tuml__concretetest__God__reason\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		fantasy(false,false,true,true,"A_<god>_<fantasy>",false,true,false,false,-1,0,true,false,true,false,true,"{\"fantasy\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<fantasy>\", \"qualified\": true, \"inverseQualified\": false, \"inverseOrdered\": true, \"unique\": true}}"),
		fakeRootFolder(false,false,true,true,"A_<god>_<fakeRootFolder>",false,true,false,false,-1,0,false,false,false,false,true,"{\"fakeRootFolder\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<fakeRootFolder>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		spirit(false,false,true,true,"A_<god>_<spirit>",false,true,false,false,-1,0,false,false,false,false,true,"{\"spirit\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<spirit>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		pet(false,false,true,false,"tumltest__org__tuml__concretetest__God__pet",false,false,true,false,1,1,false,false,false,false,true,"{\"pet\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": true, \"manyToMany\": false, \"upper\": 1, \"lower\": 1, \"label\": \"tumltest__org__tuml__concretetest__God__pet\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		world(false,false,true,true,"A_<god>_<world>",false,true,false,false,-1,0,false,false,true,false,true,"{\"world\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<world>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": true, \"unique\": true}}"),
		oneOne(false,false,true,true,"A_<god>_<oneOne>",false,true,false,false,-1,0,false,false,false,false,true,"{\"oneOne\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<oneOne>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		nonNavigableOne(false,false,true,true,"A_<god>_<nonNavigableOne>",false,true,false,false,-1,0,false,false,false,false,true,"{\"nonNavigableOne\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<nonNavigableOne>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		being(false,false,true,true,"A_<god>_<being>",false,true,false,false,-1,0,false,false,false,false,true,"{\"being\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<being>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		universe(false,false,true,true,"A_<god>_<universe>",false,true,false,false,-1,0,false,false,false,false,true,"{\"universe\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<universe>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		many1(false,false,true,true,"A_<god>_<many1>",false,true,false,false,-1,0,false,false,false,false,true,"{\"many1\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<many1>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		abstractSpecies(false,false,true,true,"A_<god>_<abstractSpecies>",false,true,false,false,-1,0,false,false,false,false,true,"{\"abstractSpecies\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<abstractSpecies>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		realRootFolder(false,false,true,true,"A_<god>_<realRootFolder>",false,true,false,false,-1,0,false,false,false,false,true,"{\"realRootFolder\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<realRootFolder>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		embeddedInteger(false,true,true,false,"tumltest__org__tuml__concretetest__God__embeddedInteger",false,false,false,true,-1,0,false,false,false,false,true,"{\"embeddedInteger\": {\"onePrimitive\": false, \"manyPrimitive\": true, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": true, \"upper\": -1, \"lower\": 0, \"label\": \"tumltest__org__tuml__concretetest__God__embeddedInteger\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		dream(false,false,true,true,"A_<god>_<dream>",false,true,false,false,-1,0,false,false,false,false,false,"{\"dream\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<dream>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": false}}"),
		nonNavigableMany(false,false,true,true,"A_<god>_<nonNavigableMany>",false,true,false,false,-1,0,false,false,false,false,true,"{\"nonNavigableMany\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<nonNavigableMany>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		rEASON(false,false,true,false,"A_<god>_<rEASON>",false,true,false,false,-1,0,false,false,false,false,true,"{\"rEASON\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<rEASON>\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		nature(false,false,true,true,"A_<god>_<nature>",false,true,false,false,-1,0,true,false,false,false,true,"{\"nature\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": true, \"oneToOne\": false, \"oneToMany\": true, \"manyToOne\": false, \"manyToMany\": false, \"upper\": -1, \"lower\": 0, \"label\": \"A_<god>_<nature>\", \"qualified\": true, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}"),
		animalFarm(false,false,true,false,"tumltest__org__tuml__concretetest__God__animalFarm",false,false,false,true,-1,0,false,false,false,false,true,"{\"animalFarm\": {\"onePrimitive\": false, \"manyPrimitive\": false, \"controllingSide\": true, \"composite\": false, \"oneToOne\": false, \"oneToMany\": false, \"manyToOne\": false, \"manyToMany\": true, \"upper\": -1, \"lower\": 0, \"label\": \"tumltest__org__tuml__concretetest__God__animalFarm\", \"qualified\": false, \"inverseQualified\": false, \"inverseOrdered\": false, \"unique\": true}}");
		private boolean onePrimitive;
		private boolean manyPrimitive;
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		private boolean qualified;
		private boolean inverseQualified;
		private boolean ordered;
		private boolean inverseOrdered;
		private boolean unique;
		private String json;
		/**
		 * constructor for GodRuntimePropertyEnum
		 * 
		 * @param onePrimitive 
		 * @param manyPrimitive 
		 * @param controllingSide 
		 * @param composite 
		 * @param label 
		 * @param oneToOne 
		 * @param oneToMany 
		 * @param manyToOne 
		 * @param manyToMany 
		 * @param upper 
		 * @param lower 
		 * @param qualified 
		 * @param inverseQualified 
		 * @param ordered 
		 * @param inverseOrdered 
		 * @param unique 
		 * @param json 
		 */
		private GodRuntimePropertyEnum(boolean onePrimitive, boolean manyPrimitive, boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower, boolean qualified, boolean inverseQualified, boolean ordered, boolean inverseOrdered, boolean unique, String json) {
			this.onePrimitive = onePrimitive;
			this.manyPrimitive = manyPrimitive;
			this.controllingSide = controllingSide;
			this.composite = composite;
			this.label = label;
			this.oneToOne = oneToOne;
			this.oneToMany = oneToMany;
			this.manyToOne = manyToOne;
			this.manyToMany = manyToMany;
			this.upper = upper;
			this.lower = lower;
			this.qualified = qualified;
			this.inverseQualified = inverseQualified;
			this.ordered = ordered;
			this.inverseOrdered = inverseOrdered;
			this.unique = unique;
			this.json = json;
		}
	
		static public String asJson() {
			StringBuilder sb = new StringBuilder();;
			sb.append("{\"God\": [");
			sb.append(GodRuntimePropertyEnum.embeddedString.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.memory.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.foot.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.oneTwo.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.many2.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.hand.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.name.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.iMany.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.angel.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.nightmare.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.demon.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.reason.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.fantasy.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.fakeRootFolder.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.spirit.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.pet.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.world.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.oneOne.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.nonNavigableOne.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.being.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.universe.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.many1.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.abstractSpecies.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.realRootFolder.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.embeddedInteger.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.dream.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.nonNavigableMany.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.rEASON.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.nature.toJson());
			sb.append(",");
			sb.append(GodRuntimePropertyEnum.animalFarm.toJson());
			sb.append("]}");
			return sb.toString();
		}
		
		static public GodRuntimePropertyEnum fromLabel(String label) {
			if ( embeddedString.getLabel().equals(label) ) {
				return embeddedString;
			}
			if ( memory.getLabel().equals(label) ) {
				return memory;
			}
			if ( foot.getLabel().equals(label) ) {
				return foot;
			}
			if ( oneTwo.getLabel().equals(label) ) {
				return oneTwo;
			}
			if ( many2.getLabel().equals(label) ) {
				return many2;
			}
			if ( hand.getLabel().equals(label) ) {
				return hand;
			}
			if ( name.getLabel().equals(label) ) {
				return name;
			}
			if ( iMany.getLabel().equals(label) ) {
				return iMany;
			}
			if ( angel.getLabel().equals(label) ) {
				return angel;
			}
			if ( nightmare.getLabel().equals(label) ) {
				return nightmare;
			}
			if ( demon.getLabel().equals(label) ) {
				return demon;
			}
			if ( reason.getLabel().equals(label) ) {
				return reason;
			}
			if ( fantasy.getLabel().equals(label) ) {
				return fantasy;
			}
			if ( fakeRootFolder.getLabel().equals(label) ) {
				return fakeRootFolder;
			}
			if ( spirit.getLabel().equals(label) ) {
				return spirit;
			}
			if ( pet.getLabel().equals(label) ) {
				return pet;
			}
			if ( world.getLabel().equals(label) ) {
				return world;
			}
			if ( oneOne.getLabel().equals(label) ) {
				return oneOne;
			}
			if ( nonNavigableOne.getLabel().equals(label) ) {
				return nonNavigableOne;
			}
			if ( being.getLabel().equals(label) ) {
				return being;
			}
			if ( universe.getLabel().equals(label) ) {
				return universe;
			}
			if ( many1.getLabel().equals(label) ) {
				return many1;
			}
			if ( abstractSpecies.getLabel().equals(label) ) {
				return abstractSpecies;
			}
			if ( realRootFolder.getLabel().equals(label) ) {
				return realRootFolder;
			}
			if ( embeddedInteger.getLabel().equals(label) ) {
				return embeddedInteger;
			}
			if ( dream.getLabel().equals(label) ) {
				return dream;
			}
			if ( nonNavigableMany.getLabel().equals(label) ) {
				return nonNavigableMany;
			}
			if ( rEASON.getLabel().equals(label) ) {
				return rEASON;
			}
			if ( nature.getLabel().equals(label) ) {
				return nature;
			}
			if ( animalFarm.getLabel().equals(label) ) {
				return animalFarm;
			}
			return null;
		}
		
		public String getJson() {
			return this.json;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public int getLower() {
			return this.lower;
		}
		
		public int getUpper() {
			return this.upper;
		}
		
		public boolean isComposite() {
			return this.composite;
		}
		
		public boolean isControllingSide() {
			return this.controllingSide;
		}
		
		public boolean isInverseOrdered() {
			return this.inverseOrdered;
		}
		
		public boolean isInverseQualified() {
			return this.inverseQualified;
		}
		
		public boolean isManyPrimitive() {
			return this.manyPrimitive;
		}
		
		public boolean isManyToMany() {
			return this.manyToMany;
		}
		
		public boolean isManyToOne() {
			return this.manyToOne;
		}
		
		public boolean isOnePrimitive() {
			return this.onePrimitive;
		}
		
		public boolean isOneToMany() {
			return this.oneToMany;
		}
		
		public boolean isOneToOne() {
			return this.oneToOne;
		}
		
		public boolean isOrdered() {
			return this.ordered;
		}
		
		public boolean isQualified() {
			return this.qualified;
		}
		
		public boolean isUnique() {
			return this.unique;
		}
		
		@Override
		public boolean isValid(int elementCount) {
			if ( isQualified() ) {
				return elementCount >= getLower();
			} else {
				return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
			}
		}
		
		@Override
		public String toJson() {
			return getJson();
		}
	
	}
}