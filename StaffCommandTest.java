package Test;

import Application.DatabaseHelper;
import Application.Question;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for staff-related commands such as flagging, unflagging, and deleting questions.
 */
public class StaffCommandTest {

    private DatabaseHelper db;

    /**
     * Sets up the in-memory database before each test.
     *
     * @throws SQLException if setup fails
     */
    @BeforeEach
    void setUp() throws SQLException {
        DatabaseHelper.DB_URL = "jdbc:h2:mem:testdb";
        db = new DatabaseHelper();
        db.connectToDatabase(true);
    }

    /**
     * Tests flagging a question and retrieving it from the list of flagged questions.
     *
     * @throws SQLException if operation fails
     */
    @Test
    void testFlaggingAndRetrievingQuestion() throws SQLException {
        db.addQuestion("What's in the box?");
        List<Question> allQuestions = db.getAllQuestions();
        assertEquals(1, allQuestions.size(), "Should be 1 question after adding");

        Question question = allQuestions.get(0);
        db.flagQuestion(question.getId());

        List<Question> flagged = db.getAllFlaggedQuestions();
        assertEquals(1, flagged.size(), "Should be 1 flagged question");
        assertEquals("What's in the box?", flagged.get(0).getText());
    }

    /**
     * Tests unflagging a previously flagged question.
     *
     * @throws SQLException if operation fails
     */
    @Test
    void testUnflaggingQuestion() throws SQLException {
        db.addQuestion("Are you not entertained?");
        int qid = db.getAllQuestions().get(0).getId();

        db.flagQuestion(qid);
        assertEquals(1, db.getAllFlaggedQuestions().size(), "Should be 1 flagged question after flagging");

        db.unflagQuestion(qid);
        assertEquals(0, db.getAllFlaggedQuestions().size(), "Should be 0 flagged questions after unflagging");
    }

    /**
     * Tests deleting a flagged question and ensuring it's removed.
     *
     * @throws SQLException if operation fails
     */
    @Test
    void testDeletingFlaggedQuestion() throws SQLException {
        db.addQuestion("Do you want to build a snowman?");
        int qid = db.getAllQuestions().get(0).getId();

        db.flagQuestion(qid);
        assertEquals(1, db.getAllFlaggedQuestions().size(), "Should be 1 flagged question before deletion");

        db.deleteQuestion(qid);
        assertEquals(0, db.getAllQuestions().size(), "Should be 0 questions after deletion");
        assertEquals(0, db.getAllFlaggedQuestions().size(), "Should be 0 flagged questions after deletion");
    }
}
