import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.statement.StatementKernel.Condition;
import components.statement.StatementKernel.Kind;
import components.utilities.Tokenizer;

/**
 * JUnit test fixture for {@code Statement}'s constructor and kernel methods.
 *
 * @author Wayne Heym
 * @author Rohan Patel, Zach Zhu
 *
 */
public abstract class StatementTest {

    /**
     * The name of a file containing a sequence of BL statements.
     */
    private static final String FILE_NAME_1 = "data/statement-sample.bl";

    /**
     * The name of a file containing a sequence of BL statements.
     */
    private static final String FILE1 = "data/statement1.bl";

    // TODO - define file names for additional test inputs

    /**
     * Invokes the {@code Statement} constructor for the implementation under
     * test and returns the result.
     *
     * @return the new statement
     * @ensures constructor = compose((BLOCK, ?, ?), <>)
     */
    protected abstract Statement constructorTest();

    /**
     * Invokes the {@code Statement} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new statement
     * @ensures constructor = compose((BLOCK, ?, ?), <>)
     */
    protected abstract Statement constructorRef();

    /**
     *
     * Creates and returns a block {@code Statement}, of the type of the
     * implementation under test, from the file with the given name.
     *
     * @param filename
     *            the name of the file to be parsed for the sequence of
     *            statements to go in the block statement
     * @return the constructed block statement
     * @ensures <pre>
     * createFromFile = [the block statement containing the statements
     * parsed from the file]
     * </pre>
     */
    private Statement createFromFileTest(String filename) {
        Statement s = this.constructorTest();
        SimpleReader file = new SimpleReader1L(filename);
        Queue<String> tokens = Tokenizer.tokens(file);
        s.parseBlock(tokens);
        file.close();
        return s;
    }

    /**
     *
     * Creates and returns a block {@code Statement}, of the reference
     * implementation type, from the file with the given name.
     *
     * @param filename
     *            the name of the file to be parsed for the sequence of
     *            statements to go in the block statement
     * @return the constructed block statement
     * @ensures <pre>
     * createFromFile = [the block statement containing the statements
     * parsed from the file]
     * </pre>
     */
    private Statement createFromFileRef(String filename) {
        Statement s = this.constructorRef();
        SimpleReader file = new SimpleReader1L(filename);
        Queue<String> tokens = Tokenizer.tokens(file);
        s.parseBlock(tokens);
        file.close();
        return s;
    }

    /**
     * Test constructor.
     */
    @Test
    public final void testConstructor() {
        /*
         * Setup
         */
        Statement sRef = this.constructorRef();

        /*
         * The call
         */
        Statement sTest = this.constructorTest();

        /*
         * Evaluation
         */
        assertEquals(sRef, sTest);
    }

    /**
     * Test kind of a WHILE statement.
     */
    @Test
    public final void testKindWhile() {
        /*
         * Setup
         */
        final int whilePos = 3;
        Statement sourceTest = this.createFromFileTest(FILE_NAME_1);
        Statement sourceRef = this.createFromFileRef(FILE_NAME_1);
        Statement sTest = sourceTest.removeFromBlock(whilePos);
        Statement sRef = sourceRef.removeFromBlock(whilePos);
        Kind kRef = sRef.kind();

        /*
         * The call
         */
        Kind kTest = sTest.kind();

        /*
         * Evaluation
         */
        assertEquals(kRef, kTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test kind of an empty block
     */
    @Test
    public final void testKindEmptyBlock() {
        /*
         * Setup
         */
        final int index = 1;
        Statement test = this.createFromFileTest(FILE1);
        Statement ref = this.createFromFileRef(FILE1);
        /*
         * The calls
         */
        Statement tIf = test.removeFromBlock(index);
        Statement rIf = ref.removeFromBlock(index);

        Statement tOut = test.newInstance();
        Statement rOut = ref.newInstance();

        Condition tCon = tIf.disassembleIf(tOut);
        Condition rCon = rIf.disassembleIf(rOut);
        /*
         * Evaluation
         */
        assertEquals(ref, test);
        assertEquals(rIf, tIf);
        assertEquals(rOut, tOut);
        assertEquals(rCon, tCon);
    }

    /**
     * Test the kind of a if.
     */
    @Test
    public final void testKindIf() {
        /*
         * Setup
         */
        final int index = 3;
        Statement test = this.createFromFileTest(FILE1);
        Statement ref = this.createFromFileRef(FILE1);

        Statement sTest = test.removeFromBlock(index);
        Statement sRef = ref.removeFromBlock(index);
        Kind kRef = sRef.kind();

        /*
         * The call
         */
        Kind kTest = sTest.kind();

        /*
         * Evaluation
         */
        assertEquals(kRef, kTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test the kind of a if-else.
     */
    @Test
    public final void testKindIfElse() {
        /*
         * Setup
         */
        final int index = 2;
        Statement test = this.createFromFileTest(FILE1);
        Statement ref = this.createFromFileRef(FILE1);

        Statement sTest = test.removeFromBlock(index);
        Statement sRef = ref.removeFromBlock(index);
        Kind kRef = sRef.kind();

        /*
         * The call
         */
        Kind kTest = sTest.kind();

        /*
         * Evaluation
         */
        assertEquals(kRef, kTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test addToBlock, add to an empty block.
     */
    @Test
    public final void testAddToBlockToEmptyBlock() {
        /*
         * Setup
         */
        Statement test = this.createFromFileTest(FILE1);
        Statement ref = this.createFromFileRef(FILE1);

        Statement nestedTest = test.removeFromBlock(1);
        Statement nestedRef = ref.removeFromBlock(1);

        Statement tAdd = test.removeFromBlock(0);
        Statement rAdd = ref.removeFromBlock(0);

        Statement tChild = test.newInstance();
        Statement rChild = ref.newInstance();

        Condition tCon = nestedTest.disassembleIf(tChild);
        Condition rCon = nestedRef.disassembleIf(rChild);

        /*
         * The call
         */
        rChild.addToBlock(0, rAdd);
        tChild.addToBlock(0, tAdd);

        /*
         * Evaluation
         */
        assertEquals(ref, test);
        assertEquals(nestedRef, nestedTest);
        assertEquals(rChild, tChild);
        assertEquals(rCon, tCon);
    }

    /**
     * Test addToBlock on the left side.
     */
    @Test
    public final void testAddToBlockLeftSide() {
        /*
         * Setup
         */
        Statement test = this.createFromFileTest(FILE1);
        Statement ref = this.createFromFileRef(FILE1);

        Statement nestedTest = test.removeFromBlock(1);
        Statement nestedRef = ref.removeFromBlock(1);
        /*
         * The call
         */
        test.addToBlock(0, nestedTest);
        ref.addToBlock(0, nestedRef);
        /*
         * Evaluation
         */
        assertEquals(nestedRef, nestedTest);
        assertEquals(ref, test);
    }

    @Test
    public final void testAddToBlockRightSide() {
        /*
         * Setup
         */
        Statement test = this.createFromFileTest(FILE1);
        Statement ref = this.createFromFileRef(FILE1);
        Statement nestedTest = test.removeFromBlock(5);
        Statement nestedRef = ref.removeFromBlock(5);
        /*
         * The calls
         */
        test.addToBlock(test.lengthOfBlock() - 1, nestedTest);
        ref.addToBlock(ref.lengthOfBlock() - 1, nestedRef);
        /*
         * Evaluation
         */
        assertEquals(nestedRef, nestedTest);
        assertEquals(ref, test);
    }

    /**
     * Test addToBlock at an interior position.
     */
    @Test
    public final void testAddToBlockInterior() {
        /*
         * Setup
         */
        Statement sTest = this.createFromFileTest(FILE_NAME_1);
        Statement sRef = this.createFromFileRef(FILE_NAME_1);
        Statement emptyBlock = sRef.newInstance();
        Statement nestedTest = sTest.removeFromBlock(1);
        Statement nestedRef = sRef.removeFromBlock(1);
        sRef.addToBlock(2, nestedRef);

        /*
         * The call
         */
        sTest.addToBlock(2, nestedTest);

        /*
         * Evaluation
         */
        assertEquals(emptyBlock, nestedTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test removeFromBlock leaving an empty block behind.
     */
    @Test
    public final void testRemoveFromBlockLeavingEmpty() {
        /*
         * Setup
         */
        Statement test = this.createFromFileTest(FILE1);
        Statement ref = this.createFromFileRef(FILE1);
        Statement nestedTest = test.removeFromBlock(5);
        Statement nestedRef = ref.removeFromBlock(5);
        Statement tChild1 = test.newInstance(), tChild2 = test.newInstance();
        Statement rChild1 = ref.newInstance(), rChild2 = ref.newInstance();
        Condition tCon = nestedTest.disassembleIfElse(tChild1, tChild2);
        Condition rCon = nestedRef.disassembleIfElse(rChild1, rChild2);
        /*
         * The call
         */
        Statement tRemoved = tChild2.removeFromBlock(0);
        Statement rRemoved = rChild2.removeFromBlock(0);
        /*
         * Evaluation
         */
        assertEquals(ref, test);
        assertEquals(nestedRef, nestedTest);
        assertEquals(rChild1, tChild1);
        assertEquals(rChild2, tChild2);
        assertEquals(rCon, tCon);
        assertEquals(rRemoved, tRemoved);
    }

    /**
     * Test removeFromBlock at the front leaving a non-empty block behind.
     */
    @Test
    public final void testRemoveFromBlockFrontLeavingNonEmpty() {
        /*
         * Setup
         */
        Statement test = this.createFromFileTest(FILE1);
        Statement ref = this.createFromFileRef(FILE1);
        /*
         * The call
         */
        Statement nestedTest = test.removeFromBlock(test.lengthOfBlock() - 1);
        Statement nestedRef = ref.removeFromBlock(ref.lengthOfBlock() - 1);
        /*
         * Evaluation
         */
        assertEquals(ref, test);
        assertEquals(nestedRef, nestedTest);
    }

    /**
     * Test removeFromBlock at the back leaving a non-empty block behind.
     */
    @Test
    public final void testRemoveFromBlockBackLeavingNonEmpty() {
        /*
         * Setup
         */
        Statement sTest = this.createFromFileTest(FILE_NAME_1);
        Statement sRef = this.createFromFileRef(FILE_NAME_1);
        Statement nestedRef = sRef.removeFromBlock(0);

        /*
         * The call
         */
        Statement nestedTest = sTest.removeFromBlock(0);

        /*
         * Evaluation
         */
        assertEquals(sRef, sTest);
        assertEquals(nestedRef, nestedTest);
    }

    /**
     * Test lengthOfBlock, greater than zero.
     */
    @Test
    public final void testLengthOfBlockNonEmpty() {
        /*
         * Setup
         */
        Statement sTest = this.createFromFileTest(FILE_NAME_1);
        Statement sRef = this.createFromFileRef(FILE_NAME_1);
        int lengthRef = sRef.lengthOfBlock();

        /*
         * The call
         */
        int lengthTest = sTest.lengthOfBlock();

        /*
         * Evaluation
         */
        assertEquals(lengthRef, lengthTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test assembleIf.
     */
    @Test
    public final void testAssembleIf() {
        /*
         * Setup
         */
        Statement blockTest = this.createFromFileTest(FILE_NAME_1);
        Statement blockRef = this.createFromFileRef(FILE_NAME_1);
        Statement emptyBlock = blockRef.newInstance();
        Statement sourceTest = blockTest.removeFromBlock(1);
        Statement sRef = blockRef.removeFromBlock(1);
        Statement nestedTest = sourceTest.newInstance();
        Condition c = sourceTest.disassembleIf(nestedTest);
        Statement sTest = sourceTest.newInstance();

        /*
         * The call
         */
        sTest.assembleIf(c, nestedTest);

        /*
         * Evaluation
         */
        assertEquals(emptyBlock, nestedTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test assembleIf.
     */
    @Test
    public final void testAssembleIfAnotherTestFileEmptyIfBody() {
        /*
         * Setup
         */
        Statement blockTest = this.createFromFileTest(FILE1);
        Statement blockRef = this.createFromFileRef(FILE1);
        Statement emptyBlock = blockRef.newInstance();
        Statement sourceTest = blockTest.removeFromBlock(1);
        Statement sRef = blockRef.removeFromBlock(1);
        Statement nestedTest = sourceTest.newInstance();
        Condition c = sourceTest.disassembleIf(nestedTest);
        Statement sTest = sourceTest.newInstance();

        /*
         * The call
         */
        sTest.assembleIf(c, nestedTest);

        /*
         * Evaluation
         */
        assertEquals(emptyBlock, nestedTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test disassembleIf.
     */
    @Test
    public final void testDisassembleIf() {
        /*
         * Setup
         */
        Statement blockTest = this.createFromFileTest(FILE_NAME_1);
        Statement blockRef = this.createFromFileRef(FILE_NAME_1);
        Statement sTest = blockTest.removeFromBlock(1);
        Statement sRef = blockRef.removeFromBlock(1);
        Statement nestedTest = sTest.newInstance();
        Statement nestedRef = sRef.newInstance();
        Condition cRef = sRef.disassembleIf(nestedRef);

        /*
         * The call
         */
        Condition cTest = sTest.disassembleIf(nestedTest);

        /*
         * Evaluation
         */
        assertEquals(nestedRef, nestedTest);
        assertEquals(sRef, sTest);
        assertEquals(cRef, cTest);
    }

    /**
     * Test disassembleIf.
     */
    @Test
    public final void testDisassembleIfAnotherTestFileEmptyBody() {
        /*
         * Setup
         */
        Statement blockTest = this.createFromFileTest(FILE1);
        Statement blockRef = this.createFromFileRef(FILE1);
        Statement sTest = blockTest.removeFromBlock(1);
        Statement sRef = blockRef.removeFromBlock(1);
        Statement nestedTest = sTest.newInstance();
        Statement nestedRef = sRef.newInstance();
        Condition cRef = sRef.disassembleIf(nestedRef);

        /*
         * The call
         */
        Condition cTest = sTest.disassembleIf(nestedTest);

        /*
         * Evaluation
         */
        assertEquals(nestedRef, nestedTest);
        assertEquals(sRef, sTest);
        assertEquals(cRef, cTest);
    }

    /**
     * Test assembleIfElse.
     */
    @Test
    public final void testAssembleIfElse() {
        /*
         * Setup
         */
        final int ifElsePos = 2;
        Statement blockTest = this.createFromFileTest(FILE_NAME_1);
        Statement blockRef = this.createFromFileRef(FILE_NAME_1);
        Statement emptyBlock = blockRef.newInstance();
        Statement sourceTest = blockTest.removeFromBlock(ifElsePos);
        Statement sRef = blockRef.removeFromBlock(ifElsePos);
        Statement thenBlockTest = sourceTest.newInstance();
        Statement elseBlockTest = sourceTest.newInstance();
        Condition cTest = sourceTest.disassembleIfElse(thenBlockTest,
                elseBlockTest);
        Statement sTest = blockTest.newInstance();

        /*
         * The call
         */
        sTest.assembleIfElse(cTest, thenBlockTest, elseBlockTest);

        /*
         * Evaluation
         */
        assertEquals(emptyBlock, thenBlockTest);
        assertEquals(emptyBlock, elseBlockTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test assembleIfElse.
     */
    @Test
    public final void testAssembleIfElseAnotherTestFileEmptyBodies() {
        /*
         * Setup
         */
        final int ifElsePos = 2;
        Statement blockTest = this.createFromFileTest(FILE1);
        Statement blockRef = this.createFromFileRef(FILE1);
        Statement emptyBlock = blockRef.newInstance();
        Statement sourceTest = blockTest.removeFromBlock(ifElsePos);
        Statement sRef = blockRef.removeFromBlock(ifElsePos);
        Statement thenBlockTest = sourceTest.newInstance();
        Statement elseBlockTest = sourceTest.newInstance();
        Condition cTest = sourceTest.disassembleIfElse(thenBlockTest,
                elseBlockTest);
        Statement sTest = blockTest.newInstance();

        /*
         * The call
         */
        sTest.assembleIfElse(cTest, thenBlockTest, elseBlockTest);

        /*
         * Evaluation
         */
        assertEquals(emptyBlock, thenBlockTest);
        assertEquals(emptyBlock, elseBlockTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test disassembleIfElse.
     */
    @Test
    public final void testDisassembleIfElse() {
        /*
         * Setup
         */
        final int ifElsePos = 2;
        Statement blockTest = this.createFromFileTest(FILE_NAME_1);
        Statement blockRef = this.createFromFileRef(FILE_NAME_1);
        Statement sTest = blockTest.removeFromBlock(ifElsePos);
        Statement sRef = blockRef.removeFromBlock(ifElsePos);
        Statement thenBlockTest = sTest.newInstance();
        Statement elseBlockTest = sTest.newInstance();
        Statement thenBlockRef = sRef.newInstance();
        Statement elseBlockRef = sRef.newInstance();
        Condition cRef = sRef.disassembleIfElse(thenBlockRef, elseBlockRef);

        /*
         * The call
         */
        Condition cTest = sTest.disassembleIfElse(thenBlockTest, elseBlockTest);

        /*
         * Evaluation
         */
        assertEquals(cRef, cTest);
        assertEquals(thenBlockRef, thenBlockTest);
        assertEquals(elseBlockRef, elseBlockTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test disassembleIfElse.
     */
    @Test
    public final void testDisassembleIfElseAnotherTestFileEmptyBodies() {
        /*
         * Setup
         */
        final int ifElsePos = 2;
        Statement blockTest = this.createFromFileTest(FILE1);
        Statement blockRef = this.createFromFileRef(FILE1);
        Statement sTest = blockTest.removeFromBlock(ifElsePos);
        Statement sRef = blockRef.removeFromBlock(ifElsePos);
        Statement thenBlockTest = sTest.newInstance();
        Statement elseBlockTest = sTest.newInstance();
        Statement thenBlockRef = sRef.newInstance();
        Statement elseBlockRef = sRef.newInstance();
        Condition cRef = sRef.disassembleIfElse(thenBlockRef, elseBlockRef);

        /*
         * The call
         */
        Condition cTest = sTest.disassembleIfElse(thenBlockTest, elseBlockTest);

        /*
         * Evaluation
         */
        assertEquals(cRef, cTest);
        assertEquals(thenBlockRef, thenBlockTest);
        assertEquals(elseBlockRef, elseBlockTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test assembleWhile.
     */
    @Test
    public final void testAssembleWhile() {
        /*
         * Setup
         */
        Statement blockTest = this.createFromFileTest(FILE_NAME_1);
        Statement blockRef = this.createFromFileRef(FILE_NAME_1);
        Statement emptyBlock = blockRef.newInstance();
        Statement sourceTest = blockTest.removeFromBlock(1);
        Statement sourceRef = blockRef.removeFromBlock(1);
        Statement nestedTest = sourceTest.newInstance();
        Statement nestedRef = sourceRef.newInstance();
        Condition cTest = sourceTest.disassembleIf(nestedTest);
        Condition cRef = sourceRef.disassembleIf(nestedRef);
        Statement sRef = sourceRef.newInstance();
        sRef.assembleWhile(cRef, nestedRef);
        Statement sTest = sourceTest.newInstance();

        /*
         * The call
         */
        sTest.assembleWhile(cTest, nestedTest);

        /*
         * Evaluation
         */
        assertEquals(emptyBlock, nestedTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test assembleWhile.
     */
    @Test
    public final void testAssembleWhileAnotherTestFile() {
        /*
         * Setup
         */
        Statement blockTest = this.createFromFileTest(FILE1);
        Statement blockRef = this.createFromFileRef(FILE1);
        Statement emptyBlock = blockRef.newInstance();
        Statement sourceTest = blockTest.removeFromBlock(1);
        Statement sourceRef = blockRef.removeFromBlock(1);
        Statement nestedTest = sourceTest.newInstance();
        Statement nestedRef = sourceRef.newInstance();
        Condition cTest = sourceTest.disassembleIf(nestedTest);
        Condition cRef = sourceRef.disassembleIf(nestedRef);
        Statement sRef = sourceRef.newInstance();
        sRef.assembleWhile(cRef, nestedRef);
        Statement sTest = sourceTest.newInstance();

        /*
         * The call
         */
        sTest.assembleWhile(cTest, nestedTest);

        /*
         * Evaluation
         */
        assertEquals(emptyBlock, nestedTest);
        assertEquals(sRef, sTest);
    }

    /**
     * Test disassembleWhile.
     */
    @Test
    public final void testDisassembleWhile() {
        /*
         * Setup
         */
        final int whilePos = 3;
        Statement blockTest = this.createFromFileTest(FILE_NAME_1);
        Statement blockRef = this.createFromFileRef(FILE_NAME_1);
        Statement sTest = blockTest.removeFromBlock(whilePos);
        Statement sRef = blockRef.removeFromBlock(whilePos);
        Statement nestedTest = sTest.newInstance();
        Statement nestedRef = sRef.newInstance();
        Condition cRef = sRef.disassembleWhile(nestedRef);

        /*
         * The call
         */
        Condition cTest = sTest.disassembleWhile(nestedTest);

        /*
         * Evaluation
         */
        assertEquals(nestedRef, nestedTest);
        assertEquals(sRef, sTest);
        assertEquals(cRef, cTest);
    }

    /**
     * Test disassembleWhile.
     */
    @Test
    public final void testDisassembleWhileAnotherFileEmptyBody() {
        /*
         * Setup
         */
        final int whilePos = 3;
        Statement blockTest = this.createFromFileTest(FILE1);
        Statement blockRef = this.createFromFileRef(FILE1);
        Statement sTest = blockTest.removeFromBlock(whilePos);
        Statement sRef = blockRef.removeFromBlock(whilePos);
        Statement nestedTest = sTest.newInstance();
        Statement nestedRef = sRef.newInstance();
        Condition cRef = sRef.disassembleWhile(nestedRef);

        /*
         * The call
         */
        Condition cTest = sTest.disassembleWhile(nestedTest);

        /*
         * Evaluation
         */
        assertEquals(nestedRef, nestedTest);
        assertEquals(sRef, sTest);
        assertEquals(cRef, cTest);
    }

    /**
     * Test assembleCall.
     */
    @Test
    public final void testAssembleCall() {
        /*
         * Setup
         */
        Statement sRef = this.constructorRef().newInstance();
        Statement sTest = this.constructorTest().newInstance();

        String name = "look-for-something";
        sRef.assembleCall(name);

        /*
         * The call
         */
        sTest.assembleCall(name);

        /*
         * Evaluation
         */
        assertEquals(sRef, sTest);
    }

    /**
     * Test disassembleCall.
     */
    @Test
    public final void testDisassembleCall() {
        /*
         * Setup
         */
        Statement blockTest = this.createFromFileTest(FILE_NAME_1);
        Statement blockRef = this.createFromFileRef(FILE_NAME_1);
        Statement sTest = blockTest.removeFromBlock(0);
        Statement sRef = blockRef.removeFromBlock(0);
        String nRef = sRef.disassembleCall();

        /*
         * The call
         */
        String nTest = sTest.disassembleCall();

        /*
         * Evaluation
         */
        assertEquals(sRef, sTest);
        assertEquals(nRef, nTest);
    }

    public static void main(String[] args) {
        Statement st1 = new Statement1();
        st1.parse(Tokenizer
                .tokens(new SimpleReader1L("data/statement1_block_empty")));
        System.out.println(st1.disassembleCall());
    }

}
