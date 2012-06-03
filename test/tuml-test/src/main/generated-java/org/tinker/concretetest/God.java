package org.tinker.concretetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.UUID;

import org.tinker.collectiontest.Dream;
import org.tinker.collectiontest.Fantasy;
import org.tinker.collectiontest.Foot;
import org.tinker.collectiontest.Hand;
import org.tinker.collectiontest.Nightmare;
import org.tinker.collectiontest.World;
import org.tinker.embeddedtest.REASON;
import org.tinker.hierarchytest.FakeRootFolder;
import org.tinker.hierarchytest.RealRootFolder;
import org.tinker.inheritencetest.AbstractSpecies;
import org.tinker.inheritencetest.Mamal;
import org.tinker.interfacetest.Being;
import org.tinker.interfacetest.IMany;
import org.tinker.interfacetest.Spirit;
import org.tinker.navigability.NonNavigableMany;
import org.tinker.navigability.NonNavigableOne;
import org.tinker.onetoone.OneOne;
import org.tinker.onetoone.OneTwo;
import org.tinker.qualifiertest.Many1;
import org.tinker.qualifiertest.Many2;
import org.tinker.qualifiertest.Nature;
import org.tuml.runtime.adaptor.GraphDb;
import org.tuml.runtime.adaptor.TinkerIdUtilFactory;
import org.tuml.runtime.adaptor.TransactionThreadEntityVar;
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerBagImpl;
import org.tuml.runtime.collection.TumlRuntimePropertyImpl;
import org.tuml.runtime.collection.TinkerOrderedSet;
import org.tuml.runtime.collection.TinkerOrderedSetImpl;
import org.tuml.runtime.collection.TinkerQualifiedBag;
import org.tuml.runtime.collection.TinkerQualifiedBagImpl;
import org.tuml.runtime.collection.TinkerQualifiedOrderedSet;
import org.tuml.runtime.collection.TinkerQualifiedOrderedSetImpl;
import org.tuml.runtime.collection.TinkerQualifiedSequence;
import org.tuml.runtime.collection.TinkerQualifiedSequenceImpl;
import org.tuml.runtime.collection.TinkerQualifiedSet;
import org.tuml.runtime.collection.TinkerQualifiedSetImpl;
import org.tuml.runtime.collection.TinkerSequence;
import org.tuml.runtime.collection.TinkerSequenceImpl;
import org.tuml.runtime.collection.TinkerSet;
import org.tuml.runtime.collection.TinkerSetImpl;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.CompositionNode;

public class God extends BaseTinker implements CompositionNode {
	private TinkerSet<AbstractSpecies> abstractSpecies;
	private TinkerSet<Angel> angel;
	private TinkerSet<Mamal> animalFarm;
	private TinkerSet<Being> being;
	private TinkerSet<Demon> demon;
	private TinkerBag<Dream> dream;
	private TinkerSet<Integer> embeddedInteger;
	private TinkerSet<String> embeddedString;
	private TinkerSet<FakeRootFolder> fakeRootFolder;
	private TinkerQualifiedOrderedSet<Fantasy> fantasy;
	private TinkerQualifiedSequence<Foot> foot;
	private TinkerSequence<Hand> hand;
	private TinkerSet<IMany> iMany;
	private TinkerSet<Many1> many1;
	private TinkerSet<Many2> many2;
	private TinkerQualifiedBag<Nightmare> memory;
	private TinkerSet<String> name;
	private TinkerQualifiedSet<Nature> nature;
	private TinkerQualifiedBag<Nightmare> nightmare;
	private TinkerSet<NonNavigableMany> nonNavigableMany;
	private TinkerSet<NonNavigableOne> nonNavigableOne;
	private TinkerSet<OneOne> oneOne;
	private TinkerSet<OneTwo> oneTwo;
	private TinkerSet<Mamal> pet;
	private TinkerSet<REASON> rEASON;
	private TinkerSet<RealRootFolder> realRootFolder;
	private TinkerSet<REASON> reason;
	private TinkerSet<Spirit> spirit;
	private TinkerSet<Universe> universe;
	private TinkerOrderedSet<World> world;

	/** Constructor for God
	 * 
	 * @param vertex 
	 */
	public God(Vertex vertex) {
		this.vertex=vertex;
		initialiseProperties();
	}
	
	/** Default constructor for God
	 */
	public God() {
	}
	
	/** Constructor for God
	 * 
	 * @param persistent 
	 */
	public God(Boolean persistent) {
		this.vertex = GraphDb.getDb().addVertex("dribble");
		TransactionThreadEntityVar.setNewEntity(this);
		defaultCreate();
		initialiseProperties();
	}

	public void addToAbstractSpecies(AbstractSpecies abstractSpecies) {
		if ( abstractSpecies != null ) {
			abstractSpecies.z_internalRemoveFromGod(abstractSpecies.getGod());
			abstractSpecies.z_internalAddToGod(this);
			z_internalAddToAbstractSpecies(abstractSpecies);
		}
	}
	
	public void addToAngel(Angel angel) {
		if ( angel != null ) {
			angel.z_internalRemoveFromGod(angel.getGod());
			angel.z_internalAddToGod(this);
			z_internalAddToAngel(angel);
		}
	}
	
	public void addToAnimalFarm(Mamal animalFarm) {
		if ( animalFarm != null ) {
			z_internalAddToAnimalFarm(animalFarm);
		}
	}
	
	public void addToBeing(Being being) {
		if ( being != null ) {
			being.z_internalRemoveFromGod(being.getGod());
			being.z_internalAddToGod(this);
			z_internalAddToBeing(being);
		}
	}
	
	public void addToDemon(Demon demon) {
		if ( demon != null ) {
			demon.z_internalRemoveFromGod(demon.getGod());
			demon.z_internalAddToGod(this);
			z_internalAddToDemon(demon);
		}
	}
	
	public void addToDream(Dream dream) {
		if ( dream != null ) {
			dream.z_internalRemoveFromGod(dream.getGod());
			dream.z_internalAddToGod(this);
			z_internalAddToDream(dream);
		}
	}
	
	public void addToEmbeddedInteger(Integer embeddedInteger) {
		if ( embeddedInteger != null ) {
			z_internalAddToEmbeddedInteger(embeddedInteger);
		}
	}
	
	public void addToEmbeddedString(String embeddedString) {
		if ( embeddedString != null ) {
			z_internalAddToEmbeddedString(embeddedString);
		}
	}
	
	public void addToFakeRootFolder(FakeRootFolder fakeRootFolder) {
		if ( fakeRootFolder != null ) {
			fakeRootFolder.z_internalRemoveFromGod(fakeRootFolder.getGod());
			fakeRootFolder.z_internalAddToGod(this);
			z_internalAddToFakeRootFolder(fakeRootFolder);
		}
	}
	
	public void addToFantasy(Fantasy fantasy) {
		if ( fantasy != null ) {
			fantasy.z_internalRemoveFromGod(fantasy.getGod());
			fantasy.z_internalAddToGod(this);
			z_internalAddToFantasy(fantasy);
		}
	}
	
	public void addToFoot(Foot foot) {
		if ( foot != null ) {
			foot.z_internalRemoveFromGod(foot.getGod());
			foot.z_internalAddToGod(this);
			z_internalAddToFoot(foot);
		}
	}
	
	public void addToHand(Hand hand) {
		if ( hand != null ) {
			hand.z_internalRemoveFromGod(hand.getGod());
			hand.z_internalAddToGod(this);
			z_internalAddToHand(hand);
		}
	}
	
	public void addToIMany(IMany iMany) {
		if ( iMany != null ) {
			iMany.z_internalRemoveFromGod(iMany.getGod());
			iMany.z_internalAddToGod(this);
			z_internalAddToIMany(iMany);
		}
	}
	
	public void addToMany1(Many1 many1) {
		if ( many1 != null ) {
			many1.z_internalRemoveFromGod(many1.getGod());
			many1.z_internalAddToGod(this);
			z_internalAddToMany1(many1);
		}
	}
	
	public void addToMany2(Many2 many2) {
		if ( many2 != null ) {
			many2.z_internalRemoveFromGod(many2.getGod());
			many2.z_internalAddToGod(this);
			z_internalAddToMany2(many2);
		}
	}
	
	public void addToMemory(Nightmare memory) {
		if ( memory != null ) {
			memory.z_internalRemoveFromGodOfMemory(memory.getGodOfMemory());
			memory.z_internalAddToGodOfMemory(this);
			z_internalAddToMemory(memory);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			z_internalAddToName(name);
		}
	}
	
	public void addToNature(Nature nature) {
		if ( nature != null ) {
			nature.z_internalRemoveFromGod(nature.getGod());
			nature.z_internalAddToGod(this);
			z_internalAddToNature(nature);
		}
	}
	
	public void addToNightmare(Nightmare nightmare) {
		if ( nightmare != null ) {
			nightmare.z_internalRemoveFromGod(nightmare.getGod());
			nightmare.z_internalAddToGod(this);
			z_internalAddToNightmare(nightmare);
		}
	}
	
	public void addToNonNavigableMany(NonNavigableMany nonNavigableMany) {
		if ( nonNavigableMany != null ) {
			nonNavigableMany.z_internalRemoveFromGod(nonNavigableMany.getGod());
			nonNavigableMany.z_internalAddToGod(this);
			z_internalAddToNonNavigableMany(nonNavigableMany);
		}
	}
	
	public void addToNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			nonNavigableOne.z_internalRemoveFromGod(nonNavigableOne.getGod());
			nonNavigableOne.z_internalAddToGod(this);
			z_internalAddToNonNavigableOne(nonNavigableOne);
		}
	}
	
	public void addToOneOne(OneOne oneOne) {
		if ( oneOne != null ) {
			oneOne.z_internalRemoveFromGod(oneOne.getGod());
			oneOne.z_internalAddToGod(this);
			z_internalAddToOneOne(oneOne);
		}
	}
	
	public void addToOneTwo(OneTwo oneTwo) {
		if ( oneTwo != null ) {
			oneTwo.z_internalRemoveFromGod(oneTwo.getGod());
			oneTwo.z_internalAddToGod(this);
			z_internalAddToOneTwo(oneTwo);
		}
	}
	
	public void addToPet(Mamal pet) {
		if ( pet != null ) {
			z_internalAddToPet(pet);
		}
	}
	
	public void addToREASON(REASON rEASON) {
		if ( rEASON != null ) {
			rEASON.z_internalRemoveFromGod(rEASON.getGod());
			rEASON.z_internalAddToGod(this);
			z_internalAddToREASON(rEASON);
		}
	}
	
	public void addToRealRootFolder(RealRootFolder realRootFolder) {
		if ( realRootFolder != null ) {
			realRootFolder.z_internalRemoveFromGod(realRootFolder.getGod());
			realRootFolder.z_internalAddToGod(this);
			z_internalAddToRealRootFolder(realRootFolder);
		}
	}
	
	public void addToReason(REASON reason) {
		if ( reason != null ) {
			z_internalAddToReason(reason);
		}
	}
	
	public void addToSpirit(Spirit spirit) {
		if ( spirit != null ) {
			spirit.z_internalRemoveFromGod(spirit.getGod());
			spirit.z_internalAddToGod(this);
			z_internalAddToSpirit(spirit);
		}
	}
	
	public void addToUniverse(Universe universe) {
		if ( universe != null ) {
			universe.z_internalRemoveFromGod(universe.getGod());
			universe.z_internalAddToGod(this);
			z_internalAddToUniverse(universe);
		}
	}
	
	public void addToWorld(World world) {
		if ( world != null ) {
			world.z_internalRemoveFromGod(world.getGod());
			world.z_internalAddToGod(this);
			z_internalAddToWorld(world);
		}
	}
	
	public void createComponents() {
	}
	
	@Override
	public void delete() {
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
	
	public TinkerQualifiedSequence<Foot> getFoot() {
		return this.foot;
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
	
	public TinkerQualifiedBag<Nightmare> getNightmare() {
		return this.nightmare;
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
	
	@Override
	public CompositionNode getOwningObject() {
		return null;
	}
	
	public Mamal getPet() {
		TinkerSet<Mamal> tmp = this.pet;
		if ( !tmp.isEmpty() ) {
			return tmp.iterator().next();
		} else {
			return null;
		}
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
	
	@Override
	public void init(CompositionNode compositeOwner) {
		this.hasInitBeenCalled = true;
		initVariables();
	}
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.name =  new TinkerSetImpl<String>(this, "org__tinker__concretetest__God__name", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
		this.universe =  new TinkerSetImpl<Universe>(this, "A_<god>_<universe>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.angel =  new TinkerSetImpl<Angel>(this, "A_<god>_<angel>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.spirit =  new TinkerSetImpl<Spirit>(this, "A_<god>_<spirit>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.being =  new TinkerSetImpl<Being>(this, "A_<god>_<being>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.abstractSpecies =  new TinkerSetImpl<AbstractSpecies>(this, "A_<god>_<abstractSpecies>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.iMany =  new TinkerSetImpl<IMany>(this, "A_<god>_<iMany>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.embeddedString =  new TinkerSetImpl<String>(this, "org__tinker__concretetest__God__embeddedString", true, new TumlRuntimePropertyImpl(false,false,false,true,0,-1), false);
		this.embeddedInteger =  new TinkerSetImpl<Integer>(this, "org__tinker__concretetest__God__embeddedInteger", true, new TumlRuntimePropertyImpl(false,false,false,true,0,-1), false);
		this.realRootFolder =  new TinkerSetImpl<RealRootFolder>(this, "A_<god>_<realRootFolder>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.fakeRootFolder =  new TinkerSetImpl<FakeRootFolder>(this, "A_<god>_<fakeRootFolder>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.reason =  new TinkerSetImpl<REASON>(this, "org__tinker__concretetest__God__reason", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
		this.pet =  new TinkerSetImpl<Mamal>(this, "org__tinker__concretetest__God__pet", true, new TumlRuntimePropertyImpl(false,false,true,false,1,1), false);
		this.animalFarm =  new TinkerSetImpl<Mamal>(this, "org__tinker__concretetest__God__animalFarm", true, new TumlRuntimePropertyImpl(false,false,false,true,0,-1), false);
		this.nature =  new TinkerQualifiedSetImpl<Nature>(this, "A_<god>_<nature>", getUid(), true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.hand =  new TinkerSequenceImpl<Hand>(this, "A_<god>_<hand>", getUid(), true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.foot =  new TinkerQualifiedSequenceImpl<Foot>(this, "A_<god>_<foot>", getUid(), true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.world =  new TinkerOrderedSetImpl<World>(this, "A_<god>_<world>", getUid(), true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.fantasy =  new TinkerQualifiedOrderedSetImpl<Fantasy>(this, "A_<god>_<fantasy>", getUid(), true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.many1 =  new TinkerSetImpl<Many1>(this, "A_<god>_<many1>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.many2 =  new TinkerSetImpl<Many2>(this, "A_<god>_<many2>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.dream =  new TinkerBagImpl<Dream>(this, "A_<god>_<dream>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.nightmare =  new TinkerQualifiedBagImpl<Nightmare>(this, "A_<god>_<nightmare>", getUid(), true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.demon =  new TinkerSetImpl<Demon>(this, "A_<god>_<demon>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.oneOne =  new TinkerSetImpl<OneOne>(this, "A_<god>_<oneOne>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.oneTwo =  new TinkerSetImpl<OneTwo>(this, "A_<god>_<oneTwo>", true, new TumlRuntimePropertyImpl(false,true,false,false,0,-1), true);
		this.nonNavigableOne =  new TinkerSetImpl<NonNavigableOne>(this, "A_<god>_<nonNavigableOne>", true, new TumlRuntimePropertyImpl(false,false,false,true,0,-1), true);
		this.nonNavigableMany =  new TinkerSetImpl<NonNavigableMany>(this, "A_<god>_<nonNavigableMany>", true, new TumlRuntimePropertyImpl(false,false,false,true,0,-1), true);
	}
	
	@Override
	public boolean isTinkerRoot() {
		return true;
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setName(TinkerSet<String> name) {
	}
	
	public void setPet(TinkerSet<Mamal> pet) {
	}
	
	public void setReason(TinkerSet<REASON> reason) {
	}
	
	public void z_internalAddToAbstractSpecies(AbstractSpecies abstractSpecies) {
		this.abstractSpecies.add(abstractSpecies);
	}
	
	public void z_internalAddToAngel(Angel angel) {
		this.angel.add(angel);
	}
	
	public void z_internalAddToAnimalFarm(Mamal animalFarm) {
		this.animalFarm.add(animalFarm);
	}
	
	public void z_internalAddToBeing(Being being) {
		this.being.add(being);
	}
	
	public void z_internalAddToDemon(Demon demon) {
		this.demon.add(demon);
	}
	
	public void z_internalAddToDream(Dream dream) {
		this.dream.add(dream);
	}
	
	public void z_internalAddToEmbeddedInteger(Integer embeddedInteger) {
		this.embeddedInteger.add(embeddedInteger);
	}
	
	public void z_internalAddToEmbeddedString(String embeddedString) {
		this.embeddedString.add(embeddedString);
	}
	
	public void z_internalAddToFakeRootFolder(FakeRootFolder fakeRootFolder) {
		this.fakeRootFolder.add(fakeRootFolder);
	}
	
	public void z_internalAddToFantasy(Fantasy fantasy) {
		this.fantasy.add(fantasy);
	}
	
	public void z_internalAddToFoot(Foot foot) {
		this.foot.add(foot);
	}
	
	public void z_internalAddToHand(Hand hand) {
		this.hand.add(hand);
	}
	
	public void z_internalAddToIMany(IMany iMany) {
		this.iMany.add(iMany);
	}
	
	public void z_internalAddToMany1(Many1 many1) {
		this.many1.add(many1);
	}
	
	public void z_internalAddToMany2(Many2 many2) {
		this.many2.add(many2);
	}
	
	public void z_internalAddToMemory(Nightmare memory) {
		this.memory.add(memory);
	}
	
	public void z_internalAddToName(String name) {
		this.name.add(name);
	}
	
	public void z_internalAddToNature(Nature nature) {
		this.nature.add(nature);
	}
	
	public void z_internalAddToNightmare(Nightmare nightmare) {
		this.nightmare.add(nightmare);
	}
	
	public void z_internalAddToNonNavigableMany(NonNavigableMany nonNavigableMany) {
		this.nonNavigableMany.add(nonNavigableMany);
	}
	
	public void z_internalAddToNonNavigableOne(NonNavigableOne nonNavigableOne) {
		this.nonNavigableOne.add(nonNavigableOne);
	}
	
	public void z_internalAddToOneOne(OneOne oneOne) {
		this.oneOne.add(oneOne);
	}
	
	public void z_internalAddToOneTwo(OneTwo oneTwo) {
		this.oneTwo.add(oneTwo);
	}
	
	public void z_internalAddToPet(Mamal pet) {
		this.pet.add(pet);
	}
	
	public void z_internalAddToREASON(REASON rEASON) {
		this.rEASON.add(rEASON);
	}
	
	public void z_internalAddToRealRootFolder(RealRootFolder realRootFolder) {
		this.realRootFolder.add(realRootFolder);
	}
	
	public void z_internalAddToReason(REASON reason) {
		this.reason.add(reason);
	}
	
	public void z_internalAddToSpirit(Spirit spirit) {
		this.spirit.add(spirit);
	}
	
	public void z_internalAddToUniverse(Universe universe) {
		this.universe.add(universe);
	}
	
	public void z_internalAddToWorld(World world) {
		this.world.add(world);
	}
	
	public void z_internalRemoveFromAbstractSpecies(AbstractSpecies abstractSpecies) {
		this.abstractSpecies.remove(abstractSpecies);
	}
	
	public void z_internalRemoveFromAngel(Angel angel) {
		this.angel.remove(angel);
	}
	
	public void z_internalRemoveFromAnimalFarm(Mamal animalFarm) {
		this.animalFarm.remove(animalFarm);
	}
	
	public void z_internalRemoveFromBeing(Being being) {
		this.being.remove(being);
	}
	
	public void z_internalRemoveFromDemon(Demon demon) {
		this.demon.remove(demon);
	}
	
	public void z_internalRemoveFromDream(Dream dream) {
		this.dream.remove(dream);
	}
	
	public void z_internalRemoveFromEmbeddedInteger(Integer embeddedInteger) {
		this.embeddedInteger.remove(embeddedInteger);
	}
	
	public void z_internalRemoveFromEmbeddedString(String embeddedString) {
		this.embeddedString.remove(embeddedString);
	}
	
	public void z_internalRemoveFromFakeRootFolder(FakeRootFolder fakeRootFolder) {
		this.fakeRootFolder.remove(fakeRootFolder);
	}
	
	public void z_internalRemoveFromFantasy(Fantasy fantasy) {
		this.fantasy.remove(fantasy);
	}
	
	public void z_internalRemoveFromFoot(Foot foot) {
		this.foot.remove(foot);
	}
	
	public void z_internalRemoveFromHand(Hand hand) {
		this.hand.remove(hand);
	}
	
	public void z_internalRemoveFromIMany(IMany iMany) {
		this.iMany.remove(iMany);
	}
	
	public void z_internalRemoveFromMany1(Many1 many1) {
		this.many1.remove(many1);
	}
	
	public void z_internalRemoveFromMany2(Many2 many2) {
		this.many2.remove(many2);
	}
	
	public void z_internalRemoveFromMemory(Nightmare memory) {
		this.memory.remove(memory);
	}
	
	public void z_internalRemoveFromName(String name) {
		this.name.remove(name);
	}
	
	public void z_internalRemoveFromNature(Nature nature) {
		this.nature.remove(nature);
	}
	
	public void z_internalRemoveFromNightmare(Nightmare nightmare) {
		this.nightmare.remove(nightmare);
	}
	
	public void z_internalRemoveFromNonNavigableMany(NonNavigableMany nonNavigableMany) {
		this.nonNavigableMany.remove(nonNavigableMany);
	}
	
	public void z_internalRemoveFromNonNavigableOne(NonNavigableOne nonNavigableOne) {
		this.nonNavigableOne.remove(nonNavigableOne);
	}
	
	public void z_internalRemoveFromOneOne(OneOne oneOne) {
		this.oneOne.remove(oneOne);
	}
	
	public void z_internalRemoveFromOneTwo(OneTwo oneTwo) {
		this.oneTwo.remove(oneTwo);
	}
	
	public void z_internalRemoveFromPet(Mamal pet) {
		this.pet.remove(pet);
	}
	
	public void z_internalRemoveFromREASON(REASON rEASON) {
		this.rEASON.remove(rEASON);
	}
	
	public void z_internalRemoveFromRealRootFolder(RealRootFolder realRootFolder) {
		this.realRootFolder.remove(realRootFolder);
	}
	
	public void z_internalRemoveFromReason(REASON reason) {
		this.reason.remove(reason);
	}
	
	public void z_internalRemoveFromSpirit(Spirit spirit) {
		this.spirit.remove(spirit);
	}
	
	public void z_internalRemoveFromUniverse(Universe universe) {
		this.universe.remove(universe);
	}
	
	public void z_internalRemoveFromWorld(World world) {
		this.world.remove(world);
	}

}