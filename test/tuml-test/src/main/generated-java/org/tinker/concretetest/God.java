package org.tinker.concretetest;

import com.tinkerpop.blueprints.pgm.Vertex;

import java.util.Collection;
import java.util.List;
import java.util.Set;
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
import org.tuml.runtime.collection.TinkerBag;
import org.tuml.runtime.collection.TinkerBagImpl;
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
import org.tuml.runtime.collection.TumlRuntimeProperty;
import org.tuml.runtime.domain.BaseTinker;
import org.tuml.runtime.domain.TinkerNode;

public class God extends BaseTinker implements TinkerNode {
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
		defaultCreate();
		initialiseProperties();
	}

	public void addToAbstractSpecies(AbstractSpecies abstractSpecies) {
		if ( abstractSpecies != null ) {
			this.abstractSpecies.add(abstractSpecies);
		}
	}
	
	public void addToAbstractSpecies(Set<AbstractSpecies> abstractSpecies) {
		if ( !abstractSpecies.isEmpty() ) {
			this.abstractSpecies.addAll(abstractSpecies);
		}
	}
	
	public void addToAngel(Angel angel) {
		if ( angel != null ) {
			this.angel.add(angel);
		}
	}
	
	public void addToAngel(Set<Angel> angel) {
		if ( !angel.isEmpty() ) {
			this.angel.addAll(angel);
		}
	}
	
	public void addToAnimalFarm(Mamal animalFarm) {
		if ( animalFarm != null ) {
			this.animalFarm.add(animalFarm);
		}
	}
	
	public void addToAnimalFarm(Set<Mamal> animalFarm) {
		if ( !animalFarm.isEmpty() ) {
			this.animalFarm.addAll(animalFarm);
		}
	}
	
	public void addToBeing(Being being) {
		if ( being != null ) {
			this.being.add(being);
		}
	}
	
	public void addToBeing(Set<Being> being) {
		if ( !being.isEmpty() ) {
			this.being.addAll(being);
		}
	}
	
	public void addToDemon(Demon demon) {
		if ( demon != null ) {
			this.demon.add(demon);
		}
	}
	
	public void addToDemon(Set<Demon> demon) {
		if ( !demon.isEmpty() ) {
			this.demon.addAll(demon);
		}
	}
	
	public void addToDream(Collection<Dream> dream) {
		if ( !dream.isEmpty() ) {
			this.dream.addAll(dream);
		}
	}
	
	public void addToDream(Dream dream) {
		if ( dream != null ) {
			this.dream.add(dream);
		}
	}
	
	public void addToEmbeddedInteger(Integer embeddedInteger) {
		if ( embeddedInteger != null ) {
			this.embeddedInteger.add(embeddedInteger);
		}
	}
	
	public void addToEmbeddedInteger(Set<Integer> embeddedInteger) {
		if ( !embeddedInteger.isEmpty() ) {
			this.embeddedInteger.addAll(embeddedInteger);
		}
	}
	
	public void addToEmbeddedString(Set<String> embeddedString) {
		if ( !embeddedString.isEmpty() ) {
			this.embeddedString.addAll(embeddedString);
		}
	}
	
	public void addToEmbeddedString(String embeddedString) {
		if ( embeddedString != null ) {
			this.embeddedString.add(embeddedString);
		}
	}
	
	public void addToFakeRootFolder(FakeRootFolder fakeRootFolder) {
		if ( fakeRootFolder != null ) {
			this.fakeRootFolder.add(fakeRootFolder);
		}
	}
	
	public void addToFakeRootFolder(Set<FakeRootFolder> fakeRootFolder) {
		if ( !fakeRootFolder.isEmpty() ) {
			this.fakeRootFolder.addAll(fakeRootFolder);
		}
	}
	
	public void addToFantasy(Fantasy fantasy) {
		if ( fantasy != null ) {
			this.fantasy.add(fantasy);
		}
	}
	
	public void addToFantasy(Set<Fantasy> fantasy) {
		if ( !fantasy.isEmpty() ) {
			this.fantasy.addAll(fantasy);
		}
	}
	
	public void addToFoot(Foot foot) {
		if ( foot != null ) {
			this.foot.add(foot);
		}
	}
	
	public void addToFoot(List<Foot> foot) {
		if ( !foot.isEmpty() ) {
			this.foot.addAll(foot);
		}
	}
	
	public void addToHand(Hand hand) {
		if ( hand != null ) {
			this.hand.add(hand);
		}
	}
	
	public void addToHand(List<Hand> hand) {
		if ( !hand.isEmpty() ) {
			this.hand.addAll(hand);
		}
	}
	
	public void addToIMany(IMany iMany) {
		if ( iMany != null ) {
			this.iMany.add(iMany);
		}
	}
	
	public void addToIMany(Set<IMany> iMany) {
		if ( !iMany.isEmpty() ) {
			this.iMany.addAll(iMany);
		}
	}
	
	public void addToMany1(Many1 many1) {
		if ( many1 != null ) {
			this.many1.add(many1);
		}
	}
	
	public void addToMany1(Set<Many1> many1) {
		if ( !many1.isEmpty() ) {
			this.many1.addAll(many1);
		}
	}
	
	public void addToMany2(Many2 many2) {
		if ( many2 != null ) {
			this.many2.add(many2);
		}
	}
	
	public void addToMany2(Set<Many2> many2) {
		if ( !many2.isEmpty() ) {
			this.many2.addAll(many2);
		}
	}
	
	public void addToMemory(Collection<Nightmare> memory) {
		if ( !memory.isEmpty() ) {
			this.memory.addAll(memory);
		}
	}
	
	public void addToMemory(Nightmare memory) {
		if ( memory != null ) {
			this.memory.add(memory);
		}
	}
	
	public void addToName(String name) {
		if ( name != null ) {
			this.name.add(name);
		}
	}
	
	public void addToNature(Nature nature) {
		if ( nature != null ) {
			this.nature.add(nature);
		}
	}
	
	public void addToNature(Set<Nature> nature) {
		if ( !nature.isEmpty() ) {
			this.nature.addAll(nature);
		}
	}
	
	public void addToNightmare(Collection<Nightmare> nightmare) {
		if ( !nightmare.isEmpty() ) {
			this.nightmare.addAll(nightmare);
		}
	}
	
	public void addToNightmare(Nightmare nightmare) {
		if ( nightmare != null ) {
			this.nightmare.add(nightmare);
		}
	}
	
	public void addToNonNavigableMany(NonNavigableMany nonNavigableMany) {
		if ( nonNavigableMany != null ) {
			this.nonNavigableMany.add(nonNavigableMany);
		}
	}
	
	public void addToNonNavigableMany(Set<NonNavigableMany> nonNavigableMany) {
		if ( !nonNavigableMany.isEmpty() ) {
			this.nonNavigableMany.addAll(nonNavigableMany);
		}
	}
	
	public void addToNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			this.nonNavigableOne.add(nonNavigableOne);
		}
	}
	
	public void addToNonNavigableOne(Set<NonNavigableOne> nonNavigableOne) {
		if ( !nonNavigableOne.isEmpty() ) {
			this.nonNavigableOne.addAll(nonNavigableOne);
		}
	}
	
	public void addToOneOne(OneOne oneOne) {
		if ( oneOne != null ) {
			this.oneOne.add(oneOne);
		}
	}
	
	public void addToOneOne(Set<OneOne> oneOne) {
		if ( !oneOne.isEmpty() ) {
			this.oneOne.addAll(oneOne);
		}
	}
	
	public void addToOneTwo(OneTwo oneTwo) {
		if ( oneTwo != null ) {
			this.oneTwo.add(oneTwo);
		}
	}
	
	public void addToOneTwo(Set<OneTwo> oneTwo) {
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
	
	public void addToREASON(Set<REASON> rEASON) {
		if ( !rEASON.isEmpty() ) {
			this.rEASON.addAll(rEASON);
		}
	}
	
	public void addToRealRootFolder(RealRootFolder realRootFolder) {
		if ( realRootFolder != null ) {
			this.realRootFolder.add(realRootFolder);
		}
	}
	
	public void addToRealRootFolder(Set<RealRootFolder> realRootFolder) {
		if ( !realRootFolder.isEmpty() ) {
			this.realRootFolder.addAll(realRootFolder);
		}
	}
	
	public void addToReason(REASON reason) {
		if ( reason != null ) {
			this.reason.add(reason);
		}
	}
	
	public void addToSpirit(Set<Spirit> spirit) {
		if ( !spirit.isEmpty() ) {
			this.spirit.addAll(spirit);
		}
	}
	
	public void addToSpirit(Spirit spirit) {
		if ( spirit != null ) {
			this.spirit.add(spirit);
		}
	}
	
	public void addToUniverse(Set<Universe> universe) {
		if ( !universe.isEmpty() ) {
			this.universe.addAll(universe);
		}
	}
	
	public void addToUniverse(Universe universe) {
		if ( universe != null ) {
			this.universe.add(universe);
		}
	}
	
	public void addToWorld(Set<World> world) {
		if ( !world.isEmpty() ) {
			this.world.addAll(world);
		}
	}
	
	public void addToWorld(World world) {
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
	
	public void initVariables() {
	}
	
	@Override
	public void initialiseProperties() {
		this.being =  new TinkerSetImpl<Being>(this, GodRuntimePropertyEnum.BEING);
		this.embeddedString =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.EMBEDDEDSTRING);
		this.foot =  new TinkerQualifiedSequenceImpl<Foot>(this, getUid(), GodRuntimePropertyEnum.FOOT);
		this.angel =  new TinkerSetImpl<Angel>(this, GodRuntimePropertyEnum.ANGEL);
		this.world =  new TinkerOrderedSetImpl<World>(this, getUid(), GodRuntimePropertyEnum.WORLD);
		this.many2 =  new TinkerSetImpl<Many2>(this, GodRuntimePropertyEnum.MANY2);
		this.demon =  new TinkerSetImpl<Demon>(this, GodRuntimePropertyEnum.DEMON);
		this.fakeRootFolder =  new TinkerSetImpl<FakeRootFolder>(this, GodRuntimePropertyEnum.FAKEROOTFOLDER);
		this.fantasy =  new TinkerQualifiedOrderedSetImpl<Fantasy>(this, getUid(), GodRuntimePropertyEnum.FANTASY);
		this.spirit =  new TinkerSetImpl<Spirit>(this, GodRuntimePropertyEnum.SPIRIT);
		this.name =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.NAME);
		this.many1 =  new TinkerSetImpl<Many1>(this, GodRuntimePropertyEnum.MANY1);
		this.embeddedInteger =  new TinkerSetImpl<Integer>(this, GodRuntimePropertyEnum.EMBEDDEDINTEGER);
		this.oneTwo =  new TinkerSetImpl<OneTwo>(this, GodRuntimePropertyEnum.ONETWO);
		this.realRootFolder =  new TinkerSetImpl<RealRootFolder>(this, GodRuntimePropertyEnum.REALROOTFOLDER);
		this.rEASON =  new TinkerSetImpl<REASON>(this, GodRuntimePropertyEnum.REASON);
		this.nature =  new TinkerQualifiedSetImpl<Nature>(this, getUid(), GodRuntimePropertyEnum.NATURE);
		this.iMany =  new TinkerSetImpl<IMany>(this, GodRuntimePropertyEnum.IMANY);
		this.animalFarm =  new TinkerSetImpl<Mamal>(this, GodRuntimePropertyEnum.ANIMALFARM);
		this.universe =  new TinkerSetImpl<Universe>(this, GodRuntimePropertyEnum.UNIVERSE);
		this.pet =  new TinkerSetImpl<Mamal>(this, GodRuntimePropertyEnum.PET);
		this.abstractSpecies =  new TinkerSetImpl<AbstractSpecies>(this, GodRuntimePropertyEnum.ABSTRACTSPECIES);
		this.memory =  new TinkerQualifiedBagImpl<Nightmare>(this, getUid(), GodRuntimePropertyEnum.MEMORY);
		this.reason =  new TinkerSetImpl<REASON>(this, GodRuntimePropertyEnum.REASON);
		this.nightmare =  new TinkerQualifiedBagImpl<Nightmare>(this, getUid(), GodRuntimePropertyEnum.NIGHTMARE);
		this.nonNavigableMany =  new TinkerSetImpl<NonNavigableMany>(this, GodRuntimePropertyEnum.NONNAVIGABLEMANY);
		this.oneOne =  new TinkerSetImpl<OneOne>(this, GodRuntimePropertyEnum.ONEONE);
		this.dream =  new TinkerBagImpl<Dream>(this, GodRuntimePropertyEnum.DREAM);
		this.hand =  new TinkerSequenceImpl<Hand>(this, getUid(), GodRuntimePropertyEnum.HAND);
		this.nonNavigableOne =  new TinkerSetImpl<NonNavigableOne>(this, GodRuntimePropertyEnum.NONNAVIGABLEONE);
	}
	
	@Override
	public void initialiseProperty(TumlRuntimeProperty tumlRuntimeProperty) {
		switch ( (GodRuntimePropertyEnum.fromLabel(tumlRuntimeProperty.getLabel())) ) {
			case NONNAVIGABLEONE:
				this.nonNavigableOne =  new TinkerSetImpl<NonNavigableOne>(this, GodRuntimePropertyEnum.NONNAVIGABLEONE);
			break;
		
			case HAND:
				this.hand =  new TinkerSequenceImpl<Hand>(this, getUid(), GodRuntimePropertyEnum.HAND);
			break;
		
			case DREAM:
				this.dream =  new TinkerBagImpl<Dream>(this, GodRuntimePropertyEnum.DREAM);
			break;
		
			case ONEONE:
				this.oneOne =  new TinkerSetImpl<OneOne>(this, GodRuntimePropertyEnum.ONEONE);
			break;
		
			case NONNAVIGABLEMANY:
				this.nonNavigableMany =  new TinkerSetImpl<NonNavigableMany>(this, GodRuntimePropertyEnum.NONNAVIGABLEMANY);
			break;
		
			case EMBEDDEDINTEGER:
				this.embeddedInteger =  new TinkerSetImpl<Integer>(this, GodRuntimePropertyEnum.EMBEDDEDINTEGER);
			break;
		
			case MANY1:
				this.many1 =  new TinkerSetImpl<Many1>(this, GodRuntimePropertyEnum.MANY1);
			break;
		
			case NAME:
				this.name =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.NAME);
			break;
		
			case SPIRIT:
				this.spirit =  new TinkerSetImpl<Spirit>(this, GodRuntimePropertyEnum.SPIRIT);
			break;
		
			case FANTASY:
				this.fantasy =  new TinkerQualifiedOrderedSetImpl<Fantasy>(this, getUid(), GodRuntimePropertyEnum.FANTASY);
			break;
		
			case FAKEROOTFOLDER:
				this.fakeRootFolder =  new TinkerSetImpl<FakeRootFolder>(this, GodRuntimePropertyEnum.FAKEROOTFOLDER);
			break;
		
			case DEMON:
				this.demon =  new TinkerSetImpl<Demon>(this, GodRuntimePropertyEnum.DEMON);
			break;
		
			case MANY2:
				this.many2 =  new TinkerSetImpl<Many2>(this, GodRuntimePropertyEnum.MANY2);
			break;
		
			case WORLD:
				this.world =  new TinkerOrderedSetImpl<World>(this, getUid(), GodRuntimePropertyEnum.WORLD);
			break;
		
			case ANGEL:
				this.angel =  new TinkerSetImpl<Angel>(this, GodRuntimePropertyEnum.ANGEL);
			break;
		
			case FOOT:
				this.foot =  new TinkerQualifiedSequenceImpl<Foot>(this, getUid(), GodRuntimePropertyEnum.FOOT);
			break;
		
			case EMBEDDEDSTRING:
				this.embeddedString =  new TinkerSetImpl<String>(this, GodRuntimePropertyEnum.EMBEDDEDSTRING);
			break;
		
			case BEING:
				this.being =  new TinkerSetImpl<Being>(this, GodRuntimePropertyEnum.BEING);
			break;
		
			case ONETWO:
				this.oneTwo =  new TinkerSetImpl<OneTwo>(this, GodRuntimePropertyEnum.ONETWO);
			break;
		
			case REALROOTFOLDER:
				this.realRootFolder =  new TinkerSetImpl<RealRootFolder>(this, GodRuntimePropertyEnum.REALROOTFOLDER);
			break;
		
			case REASON:
				this.rEASON =  new TinkerSetImpl<REASON>(this, GodRuntimePropertyEnum.REASON);
			break;
		
			case NATURE:
				this.nature =  new TinkerQualifiedSetImpl<Nature>(this, getUid(), GodRuntimePropertyEnum.NATURE);
			break;
		
			case IMANY:
				this.iMany =  new TinkerSetImpl<IMany>(this, GodRuntimePropertyEnum.IMANY);
			break;
		
			case ANIMALFARM:
				this.animalFarm =  new TinkerSetImpl<Mamal>(this, GodRuntimePropertyEnum.ANIMALFARM);
			break;
		
			case UNIVERSE:
				this.universe =  new TinkerSetImpl<Universe>(this, GodRuntimePropertyEnum.UNIVERSE);
			break;
		
			case PET:
				this.pet =  new TinkerSetImpl<Mamal>(this, GodRuntimePropertyEnum.PET);
			break;
		
			case ABSTRACTSPECIES:
				this.abstractSpecies =  new TinkerSetImpl<AbstractSpecies>(this, GodRuntimePropertyEnum.ABSTRACTSPECIES);
			break;
		
			case MEMORY:
				this.memory =  new TinkerQualifiedBagImpl<Nightmare>(this, getUid(), GodRuntimePropertyEnum.MEMORY);
			break;
		
			case REASON:
				this.reason =  new TinkerSetImpl<REASON>(this, GodRuntimePropertyEnum.REASON);
			break;
		
			case NIGHTMARE:
				this.nightmare =  new TinkerQualifiedBagImpl<Nightmare>(this, getUid(), GodRuntimePropertyEnum.NIGHTMARE);
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
	
	public void removeFromAbstractSpecies(Set<AbstractSpecies> abstractSpecies) {
		if ( !abstractSpecies.isEmpty() ) {
			this.abstractSpecies.removeAll(abstractSpecies);
		}
	}
	
	public void removeFromAngel(Angel angel) {
		if ( angel != null ) {
			this.angel.remove(angel);
		}
	}
	
	public void removeFromAngel(Set<Angel> angel) {
		if ( !angel.isEmpty() ) {
			this.angel.removeAll(angel);
		}
	}
	
	public void removeFromAnimalFarm(Mamal animalFarm) {
		if ( animalFarm != null ) {
			this.animalFarm.remove(animalFarm);
		}
	}
	
	public void removeFromAnimalFarm(Set<Mamal> animalFarm) {
		if ( !animalFarm.isEmpty() ) {
			this.animalFarm.removeAll(animalFarm);
		}
	}
	
	public void removeFromBeing(Being being) {
		if ( being != null ) {
			this.being.remove(being);
		}
	}
	
	public void removeFromBeing(Set<Being> being) {
		if ( !being.isEmpty() ) {
			this.being.removeAll(being);
		}
	}
	
	public void removeFromDemon(Demon demon) {
		if ( demon != null ) {
			this.demon.remove(demon);
		}
	}
	
	public void removeFromDemon(Set<Demon> demon) {
		if ( !demon.isEmpty() ) {
			this.demon.removeAll(demon);
		}
	}
	
	public void removeFromDream(Collection<Dream> dream) {
		if ( !dream.isEmpty() ) {
			this.dream.removeAll(dream);
		}
	}
	
	public void removeFromDream(Dream dream) {
		if ( dream != null ) {
			this.dream.remove(dream);
		}
	}
	
	public void removeFromEmbeddedInteger(Integer embeddedInteger) {
		if ( embeddedInteger != null ) {
			this.embeddedInteger.remove(embeddedInteger);
		}
	}
	
	public void removeFromEmbeddedInteger(Set<Integer> embeddedInteger) {
		if ( !embeddedInteger.isEmpty() ) {
			this.embeddedInteger.removeAll(embeddedInteger);
		}
	}
	
	public void removeFromEmbeddedString(Set<String> embeddedString) {
		if ( !embeddedString.isEmpty() ) {
			this.embeddedString.removeAll(embeddedString);
		}
	}
	
	public void removeFromEmbeddedString(String embeddedString) {
		if ( embeddedString != null ) {
			this.embeddedString.remove(embeddedString);
		}
	}
	
	public void removeFromFakeRootFolder(FakeRootFolder fakeRootFolder) {
		if ( fakeRootFolder != null ) {
			this.fakeRootFolder.remove(fakeRootFolder);
		}
	}
	
	public void removeFromFakeRootFolder(Set<FakeRootFolder> fakeRootFolder) {
		if ( !fakeRootFolder.isEmpty() ) {
			this.fakeRootFolder.removeAll(fakeRootFolder);
		}
	}
	
	public void removeFromFantasy(Fantasy fantasy) {
		if ( fantasy != null ) {
			this.fantasy.remove(fantasy);
		}
	}
	
	public void removeFromFantasy(Set<Fantasy> fantasy) {
		if ( !fantasy.isEmpty() ) {
			this.fantasy.removeAll(fantasy);
		}
	}
	
	public void removeFromFoot(Foot foot) {
		if ( foot != null ) {
			this.foot.remove(foot);
		}
	}
	
	public void removeFromFoot(List<Foot> foot) {
		if ( !foot.isEmpty() ) {
			this.foot.removeAll(foot);
		}
	}
	
	public void removeFromHand(Hand hand) {
		if ( hand != null ) {
			this.hand.remove(hand);
		}
	}
	
	public void removeFromHand(List<Hand> hand) {
		if ( !hand.isEmpty() ) {
			this.hand.removeAll(hand);
		}
	}
	
	public void removeFromIMany(IMany iMany) {
		if ( iMany != null ) {
			this.iMany.remove(iMany);
		}
	}
	
	public void removeFromIMany(Set<IMany> iMany) {
		if ( !iMany.isEmpty() ) {
			this.iMany.removeAll(iMany);
		}
	}
	
	public void removeFromMany1(Many1 many1) {
		if ( many1 != null ) {
			this.many1.remove(many1);
		}
	}
	
	public void removeFromMany1(Set<Many1> many1) {
		if ( !many1.isEmpty() ) {
			this.many1.removeAll(many1);
		}
	}
	
	public void removeFromMany2(Many2 many2) {
		if ( many2 != null ) {
			this.many2.remove(many2);
		}
	}
	
	public void removeFromMany2(Set<Many2> many2) {
		if ( !many2.isEmpty() ) {
			this.many2.removeAll(many2);
		}
	}
	
	public void removeFromMemory(Collection<Nightmare> memory) {
		if ( !memory.isEmpty() ) {
			this.memory.removeAll(memory);
		}
	}
	
	public void removeFromMemory(Nightmare memory) {
		if ( memory != null ) {
			this.memory.remove(memory);
		}
	}
	
	public void removeFromName(Set<String> name) {
		if ( !name.isEmpty() ) {
			this.name.removeAll(name);
		}
	}
	
	public void removeFromName(String name) {
		if ( name != null ) {
			this.name.remove(name);
		}
	}
	
	public void removeFromNature(Nature nature) {
		if ( nature != null ) {
			this.nature.remove(nature);
		}
	}
	
	public void removeFromNature(Set<Nature> nature) {
		if ( !nature.isEmpty() ) {
			this.nature.removeAll(nature);
		}
	}
	
	public void removeFromNightmare(Collection<Nightmare> nightmare) {
		if ( !nightmare.isEmpty() ) {
			this.nightmare.removeAll(nightmare);
		}
	}
	
	public void removeFromNightmare(Nightmare nightmare) {
		if ( nightmare != null ) {
			this.nightmare.remove(nightmare);
		}
	}
	
	public void removeFromNonNavigableMany(NonNavigableMany nonNavigableMany) {
		if ( nonNavigableMany != null ) {
			this.nonNavigableMany.remove(nonNavigableMany);
		}
	}
	
	public void removeFromNonNavigableMany(Set<NonNavigableMany> nonNavigableMany) {
		if ( !nonNavigableMany.isEmpty() ) {
			this.nonNavigableMany.removeAll(nonNavigableMany);
		}
	}
	
	public void removeFromNonNavigableOne(NonNavigableOne nonNavigableOne) {
		if ( nonNavigableOne != null ) {
			this.nonNavigableOne.remove(nonNavigableOne);
		}
	}
	
	public void removeFromNonNavigableOne(Set<NonNavigableOne> nonNavigableOne) {
		if ( !nonNavigableOne.isEmpty() ) {
			this.nonNavigableOne.removeAll(nonNavigableOne);
		}
	}
	
	public void removeFromOneOne(OneOne oneOne) {
		if ( oneOne != null ) {
			this.oneOne.remove(oneOne);
		}
	}
	
	public void removeFromOneOne(Set<OneOne> oneOne) {
		if ( !oneOne.isEmpty() ) {
			this.oneOne.removeAll(oneOne);
		}
	}
	
	public void removeFromOneTwo(OneTwo oneTwo) {
		if ( oneTwo != null ) {
			this.oneTwo.remove(oneTwo);
		}
	}
	
	public void removeFromOneTwo(Set<OneTwo> oneTwo) {
		if ( !oneTwo.isEmpty() ) {
			this.oneTwo.removeAll(oneTwo);
		}
	}
	
	public void removeFromPet(Mamal pet) {
		if ( pet != null ) {
			this.pet.remove(pet);
		}
	}
	
	public void removeFromPet(Set<Mamal> pet) {
		if ( !pet.isEmpty() ) {
			this.pet.removeAll(pet);
		}
	}
	
	public void removeFromREASON(REASON rEASON) {
		if ( rEASON != null ) {
			this.rEASON.remove(rEASON);
		}
	}
	
	public void removeFromREASON(Set<REASON> rEASON) {
		if ( !rEASON.isEmpty() ) {
			this.rEASON.removeAll(rEASON);
		}
	}
	
	public void removeFromRealRootFolder(RealRootFolder realRootFolder) {
		if ( realRootFolder != null ) {
			this.realRootFolder.remove(realRootFolder);
		}
	}
	
	public void removeFromRealRootFolder(Set<RealRootFolder> realRootFolder) {
		if ( !realRootFolder.isEmpty() ) {
			this.realRootFolder.removeAll(realRootFolder);
		}
	}
	
	public void removeFromReason(REASON reason) {
		if ( reason != null ) {
			this.reason.remove(reason);
		}
	}
	
	public void removeFromReason(Set<REASON> reason) {
		if ( !reason.isEmpty() ) {
			this.reason.removeAll(reason);
		}
	}
	
	public void removeFromSpirit(Set<Spirit> spirit) {
		if ( !spirit.isEmpty() ) {
			this.spirit.removeAll(spirit);
		}
	}
	
	public void removeFromSpirit(Spirit spirit) {
		if ( spirit != null ) {
			this.spirit.remove(spirit);
		}
	}
	
	public void removeFromUniverse(Set<Universe> universe) {
		if ( !universe.isEmpty() ) {
			this.universe.removeAll(universe);
		}
	}
	
	public void removeFromUniverse(Universe universe) {
		if ( universe != null ) {
			this.universe.remove(universe);
		}
	}
	
	public void removeFromWorld(Set<World> world) {
		if ( !world.isEmpty() ) {
			this.world.removeAll(world);
		}
	}
	
	public void removeFromWorld(World world) {
		if ( world != null ) {
			this.world.remove(world);
		}
	}
	
	public void setAbstractSpecies(Set<AbstractSpecies> abstractSpecies) {
		clearAbstractSpecies();
		addToAbstractSpecies(abstractSpecies);
	}
	
	public void setAngel(Set<Angel> angel) {
		clearAngel();
		addToAngel(angel);
	}
	
	public void setAnimalFarm(Set<Mamal> animalFarm) {
		clearAnimalFarm();
		addToAnimalFarm(animalFarm);
	}
	
	public void setBeing(Set<Being> being) {
		clearBeing();
		addToBeing(being);
	}
	
	public void setDemon(Set<Demon> demon) {
		clearDemon();
		addToDemon(demon);
	}
	
	public void setDream(Collection<Dream> dream) {
		clearDream();
		addToDream(dream);
	}
	
	public void setEmbeddedInteger(Set<Integer> embeddedInteger) {
		clearEmbeddedInteger();
		addToEmbeddedInteger(embeddedInteger);
	}
	
	public void setEmbeddedString(Set<String> embeddedString) {
		clearEmbeddedString();
		addToEmbeddedString(embeddedString);
	}
	
	public void setFakeRootFolder(Set<FakeRootFolder> fakeRootFolder) {
		clearFakeRootFolder();
		addToFakeRootFolder(fakeRootFolder);
	}
	
	public void setFantasy(Set<Fantasy> fantasy) {
		clearFantasy();
		addToFantasy(fantasy);
	}
	
	public void setFoot(List<Foot> foot) {
		clearFoot();
		addToFoot(foot);
	}
	
	public void setHand(List<Hand> hand) {
		clearHand();
		addToHand(hand);
	}
	
	public void setIMany(Set<IMany> iMany) {
		clearIMany();
		addToIMany(iMany);
	}
	
	@Override
	public void setId(Long id) {
		TinkerIdUtilFactory.getIdUtil().setId(this.vertex, id);
	}
	
	public void setMany1(Set<Many1> many1) {
		clearMany1();
		addToMany1(many1);
	}
	
	public void setMany2(Set<Many2> many2) {
		clearMany2();
		addToMany2(many2);
	}
	
	public void setMemory(Collection<Nightmare> memory) {
		clearMemory();
		addToMemory(memory);
	}
	
	public void setName(String name) {
		clearName();
		addToName(name);
	}
	
	public void setNature(Set<Nature> nature) {
		clearNature();
		addToNature(nature);
	}
	
	public void setNightmare(Collection<Nightmare> nightmare) {
		clearNightmare();
		addToNightmare(nightmare);
	}
	
	public void setNonNavigableMany(Set<NonNavigableMany> nonNavigableMany) {
		clearNonNavigableMany();
		addToNonNavigableMany(nonNavigableMany);
	}
	
	public void setNonNavigableOne(Set<NonNavigableOne> nonNavigableOne) {
		clearNonNavigableOne();
		addToNonNavigableOne(nonNavigableOne);
	}
	
	public void setOneOne(Set<OneOne> oneOne) {
		clearOneOne();
		addToOneOne(oneOne);
	}
	
	public void setOneTwo(Set<OneTwo> oneTwo) {
		clearOneTwo();
		addToOneTwo(oneTwo);
	}
	
	public void setPet(Mamal pet) {
		clearPet();
		addToPet(pet);
	}
	
	public void setREASON(Set<REASON> rEASON) {
		clearREASON();
		addToREASON(rEASON);
	}
	
	public void setRealRootFolder(Set<RealRootFolder> realRootFolder) {
		clearRealRootFolder();
		addToRealRootFolder(realRootFolder);
	}
	
	public void setReason(REASON reason) {
		clearReason();
		addToReason(reason);
	}
	
	public void setSpirit(Set<Spirit> spirit) {
		clearSpirit();
		addToSpirit(spirit);
	}
	
	public void setUniverse(Set<Universe> universe) {
		clearUniverse();
		addToUniverse(universe);
	}
	
	public void setWorld(Set<World> world) {
		clearWorld();
		addToWorld(world);
	}

	public enum GodRuntimePropertyEnum implements TumlRuntimeProperty {
		BEING(true,true,"A_<god>_<being>",false,true,false,false,-1,0),
		EMBEDDEDSTRING(true,false,"org__tinker__concretetest__God__embeddedString",false,false,false,true,-1,0),
		FOOT(true,true,"A_<god>_<foot>",false,true,false,false,-1,0),
		ANGEL(true,true,"A_<god>_<angel>",false,true,false,false,-1,0),
		WORLD(true,true,"A_<god>_<world>",false,true,false,false,-1,0),
		MANY2(true,true,"A_<god>_<many2>",false,true,false,false,-1,0),
		DEMON(true,true,"A_<god>_<demon>",false,true,false,false,-1,0),
		FAKEROOTFOLDER(true,true,"A_<god>_<fakeRootFolder>",false,true,false,false,-1,0),
		FANTASY(true,true,"A_<god>_<fantasy>",false,true,false,false,-1,0),
		SPIRIT(true,true,"A_<god>_<spirit>",false,true,false,false,-1,0),
		NAME(true,false,"org__tinker__concretetest__God__name",false,false,true,false,1,1),
		MANY1(true,true,"A_<god>_<many1>",false,true,false,false,-1,0),
		EMBEDDEDINTEGER(true,false,"org__tinker__concretetest__God__embeddedInteger",false,false,false,true,-1,0),
		ONETWO(true,true,"A_<god>_<oneTwo>",false,true,false,false,-1,0),
		REALROOTFOLDER(true,true,"A_<god>_<realRootFolder>",false,true,false,false,-1,0),
		REASON(true,false,"A_<god>_<rEASON>",false,true,false,false,-1,0),
		NATURE(true,true,"A_<god>_<nature>",false,true,false,false,-1,0),
		IMANY(true,true,"A_<god>_<iMany>",false,true,false,false,-1,0),
		ANIMALFARM(true,false,"org__tinker__concretetest__God__animalFarm",false,false,false,true,-1,0),
		UNIVERSE(true,true,"A_<god>_<universe>",false,true,false,false,-1,0),
		PET(true,false,"org__tinker__concretetest__God__pet",false,false,true,false,1,1),
		ABSTRACTSPECIES(true,true,"A_<god>_<abstractSpecies>",false,true,false,false,-1,0),
		MEMORY(true,false,"A_<god>_<nightmare>_2",false,true,false,false,-1,0),
		REASON(true,false,"org__tinker__concretetest__God__reason",false,false,true,false,1,1),
		NIGHTMARE(true,true,"A_<god>_<nightmare>",false,true,false,false,-1,0),
		NONNAVIGABLEMANY(true,true,"A_<god>_<nonNavigableMany>",false,true,false,false,-1,0),
		ONEONE(true,true,"A_<god>_<oneOne>",false,true,false,false,-1,0),
		DREAM(true,true,"A_<god>_<dream>",false,true,false,false,-1,0),
		HAND(true,true,"A_<god>_<hand>",false,true,false,false,-1,0),
		NONNAVIGABLEONE(true,true,"A_<god>_<nonNavigableOne>",false,true,false,false,-1,0);
		private boolean controllingSide;
		private boolean composite;
		private String label;
		private boolean oneToOne;
		private boolean oneToMany;
		private boolean manyToOne;
		private boolean manyToMany;
		private int upper;
		private int lower;
		/** Constructor for GodRuntimePropertyEnum
		 * 
		 * @param controllingSide 
		 * @param composite 
		 * @param label 
		 * @param oneToOne 
		 * @param oneToMany 
		 * @param manyToOne 
		 * @param manyToMany 
		 * @param upper 
		 * @param lower 
		 */
		private GodRuntimePropertyEnum(boolean controllingSide, boolean composite, String label, boolean oneToOne, boolean oneToMany, boolean manyToOne, boolean manyToMany, int upper, int lower) {
			this.controllingSide = controllingSide;
			this.composite = composite;
			this.label = label;
			this.oneToOne = oneToOne;
			this.oneToMany = oneToMany;
			this.manyToOne = manyToOne;
			this.manyToMany = manyToMany;
			this.upper = upper;
			this.lower = lower;
		}
	
		static public GodRuntimePropertyEnum fromLabel(String label) {
			if ( BEING.getLabel().equals(label) ) {
				return BEING;
			}
			if ( EMBEDDEDSTRING.getLabel().equals(label) ) {
				return EMBEDDEDSTRING;
			}
			if ( FOOT.getLabel().equals(label) ) {
				return FOOT;
			}
			if ( ANGEL.getLabel().equals(label) ) {
				return ANGEL;
			}
			if ( WORLD.getLabel().equals(label) ) {
				return WORLD;
			}
			if ( MANY2.getLabel().equals(label) ) {
				return MANY2;
			}
			if ( DEMON.getLabel().equals(label) ) {
				return DEMON;
			}
			if ( FAKEROOTFOLDER.getLabel().equals(label) ) {
				return FAKEROOTFOLDER;
			}
			if ( FANTASY.getLabel().equals(label) ) {
				return FANTASY;
			}
			if ( SPIRIT.getLabel().equals(label) ) {
				return SPIRIT;
			}
			if ( NAME.getLabel().equals(label) ) {
				return NAME;
			}
			if ( MANY1.getLabel().equals(label) ) {
				return MANY1;
			}
			if ( EMBEDDEDINTEGER.getLabel().equals(label) ) {
				return EMBEDDEDINTEGER;
			}
			if ( ONETWO.getLabel().equals(label) ) {
				return ONETWO;
			}
			if ( REALROOTFOLDER.getLabel().equals(label) ) {
				return REALROOTFOLDER;
			}
			if ( REASON.getLabel().equals(label) ) {
				return REASON;
			}
			if ( NATURE.getLabel().equals(label) ) {
				return NATURE;
			}
			if ( IMANY.getLabel().equals(label) ) {
				return IMANY;
			}
			if ( ANIMALFARM.getLabel().equals(label) ) {
				return ANIMALFARM;
			}
			if ( UNIVERSE.getLabel().equals(label) ) {
				return UNIVERSE;
			}
			if ( PET.getLabel().equals(label) ) {
				return PET;
			}
			if ( ABSTRACTSPECIES.getLabel().equals(label) ) {
				return ABSTRACTSPECIES;
			}
			if ( MEMORY.getLabel().equals(label) ) {
				return MEMORY;
			}
			if ( REASON.getLabel().equals(label) ) {
				return REASON;
			}
			if ( NIGHTMARE.getLabel().equals(label) ) {
				return NIGHTMARE;
			}
			if ( NONNAVIGABLEMANY.getLabel().equals(label) ) {
				return NONNAVIGABLEMANY;
			}
			if ( ONEONE.getLabel().equals(label) ) {
				return ONEONE;
			}
			if ( DREAM.getLabel().equals(label) ) {
				return DREAM;
			}
			if ( HAND.getLabel().equals(label) ) {
				return HAND;
			}
			if ( NONNAVIGABLEONE.getLabel().equals(label) ) {
				return NONNAVIGABLEONE;
			}
			throw new IllegalStateException();
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
		
		public boolean isManyToMany() {
			return this.manyToMany;
		}
		
		public boolean isManyToOne() {
			return this.manyToOne;
		}
		
		public boolean isOneToMany() {
			return this.oneToMany;
		}
		
		public boolean isOneToOne() {
			return this.oneToOne;
		}
		
		@Override
		public boolean isValid(int elementCount) {
			return (getUpper() == -1 || elementCount <= getUpper()) && elementCount >= getLower();
		}
	
	}
}