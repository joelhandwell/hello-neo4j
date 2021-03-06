package hello;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {

    private final GraphDatabaseService graphDb;

    public App(){
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File("graph.db"));
        Runtime.getRuntime().addShutdownHook(new Thread( graphDb::shutdown ) );
    }

    private static enum RelTypes implements RelationshipType{
        KNOWS
    }

    public String getGreeting() {

        String greet;

        try (Transaction tx = graphDb.beginTx()){

            Node firstNode;
            Node secondNode;
            Relationship relationship;

            firstNode = graphDb.createNode();
            firstNode.setProperty("message", "Hello " );

            secondNode = graphDb.createNode();
            secondNode.setProperty("message", "world.");

            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            relationship.setProperty("message", "with Neo4j");

            greet = (String) firstNode.getProperty("message") + secondNode.getProperty("message");

            firstNode.getSingleRelationship(RelTypes.KNOWS, Direction.OUTGOING).delete();

            firstNode.delete();
            secondNode.delete();

            tx.success();
        }

        return greet;
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
