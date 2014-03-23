package org.umlg.runtime.restlet;

/**
 * Date: 2012/12/26
 * Time: 6:01 PM
 */
public class UmlgTransactionServerResourceImpl /*extends ServerResource implements UmlgTransactionServerResource*/ {

//    @Override
//    public Representation delete(Representation entity) {
//        String uid = getQueryValue("transactionIdentifier");
//        try {
//            String commitValue = entity.getText();
//            TransactionIdentifier transactionIdentifier = UmlgTransactionManager.INSTANCE.get(uid);
//            GraphDb.get().resume(transactionIdentifier);
//            GraphDb.get().rollback();
//            return new StringRepresentation("rolled back");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * This creates a transaction and suspends it immediately
//     * @param entity
//     * @return
//     */
//    @Override
//    public Representation post(Representation entity) {
//        TransactionIdentifier transactionIdentifier = GraphDb.get().suspend();
//        UmlgTransactionManager.INSTANCE.put(transactionIdentifier);
//        return new JsonRepresentation(transactionIdentifier.toJson());
//    }
//
//    @Override
//    public Representation put(Representation entity) {
//        String uid = getQueryValue("transactionIdentifier");
//        try {
//            String commitValue = entity.getText();
//            TransactionIdentifier transactionIdentifier = UmlgTransactionManager.INSTANCE.get(uid);
//            GraphDb.get().resume(transactionIdentifier);
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Boolean> commitValueAsMap = objectMapper.readValue(commitValue, Map.class);
//            if (commitValueAsMap.get(UmlgTransactionServerResource.COMMIT)) {
//                GraphDb.get().commit();
//                return new JsonRepresentation("{\"transaction\":\"COMMITTED\"}");
//            } else {
//                GraphDb.get().rollback();
//                return new JsonRepresentation("{\"transaction\":\"ROLLED_BACK\"}");
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
