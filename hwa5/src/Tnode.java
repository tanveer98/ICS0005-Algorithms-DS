import java.util.*;

public class Tnode {

    private String name;
    private Tnode firstChild;
    private Tnode nextSibling;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tnode getFirstChild() {
        return this.firstChild;
    }

    public void setFirstChild(Tnode firstChild) {
        this.firstChild = firstChild;
    }

    public Tnode getNextSibling() {
        return this.nextSibling;
    }

    public void setNextSibling(Tnode nextSibling) {
        this.nextSibling = nextSibling;
    }

    /*
     * if it has a child, it is followed by '(' if it has a sibiling, it is followed
     * by ',' name ')'
     */
    private static void traverse(Tnode node, StringBuffer b) {
        if (node == null) {
            return;
        }
        String name = node.getName();
        Tnode child = node.getFirstChild(), sib = node.getNextSibling();

        b.append(name); // traverse root node!;

        if (child != null)
            b.append("(");
        traverse(node.getFirstChild(), b); // traverse child

        if (sib != null)
            b.append(",");

        traverse(node.getNextSibling(), b); // traverse sibiling

        if (sib != null)
            b.append(")");
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        Tnode root = this;
        traverse(root, b);
        return b.toString();
    }

    private static boolean isOperator(String op) {
        String regex = "^[-+*/]|(ROT)|(SWAP)$";
        return op.matches(regex);
    }

    private static boolean isOperand(String op) {
        String regex = "^-?[0-9]+$";
        return op.matches(regex);
    }

    private static boolean isValid(String token) {
        return (isOperand(token) || isOperator(token));
    }

    public static Tnode buildFromRPN(String pol) {
        /*
         * In essence converts Postfix (RPN) to a prefix (LISP style) expression,using
         * trees. Algorithm: If char is operand, Create TNode.name = operand, add it to
         * stack If char is operator, b = pop(), a = pop(), create TNode.name =
         * operator, set child a, child.sibilng = b
         */
        Tnode root = null;
        // TODO!!!
        Deque<Tnode> stack = new ArrayDeque<>();
        StringTokenizer st = new StringTokenizer(pol);

        while (st.hasMoreTokens()) {
            Tnode node = null;
            String token = st.nextToken();
            if (!isValid(token)) {
                throw new RuntimeException("Invalid token '" + token + "' In RPN string :" + pol);
            }

            // add token to the stack
            node = new Tnode();
            node.setName(token);

            if (isOperator(token)) {
                // actually an operator, pop two values, add it to the root node.
                if (token.equals("ROT")) {
                    // rotate
                    if (stack.size() < 3) {
                        throw new RuntimeException("Invalid operation ROT for RPN string:" + pol);
                    }

                    Tnode c = stack.pop();
                    Tnode b = stack.pop();
                    Tnode a = stack.pop();

                    stack.push(b);
                    stack.push(c);
                    stack.push(a);
                    continue; //so ROT isnt in tree

                } else if (token.equals("SWAP")) {
                    // swap
                    if (stack.size() < 2) {
                        throw new RuntimeException("Invalid operation SWAP for RPN string:" + pol);
                    }
                    Tnode b = stack.pop(); // subtree 1
                    Tnode a = stack.pop(); // subtree 2

                    stack.push(b);
                    stack.push(a);
                    continue; //so SWAP isnt in tree
                } else {
                    // normal operand.
                    node = new Tnode();
                    node.setName(token);
                    if (stack.size() < 2) {
                        throw new RuntimeException("Invalid RPN String: " + pol + " Reason: too few elements!");
                    }
                    Tnode b = stack.pop();
                    Tnode a = stack.pop();

                    if (a == null || b == null) {
                        throw new RuntimeException("Invalid RPN String: " + pol);
                    }
                    a.setNextSibling(b);
                    node.setFirstChild(a);
                }
            }
            stack.push(node);
        }

        root = stack.peek();

        if (!isOperator(root.getName())) {
            if (stack.size() > 1)
                throw new RuntimeException("Invalid RPN String: " + pol + " Reason: too many elements!");
        }
        return root;
    }

    public static void main(String[] param) {
        String rpn = "2 5 9 ROT + SWAP -";
        System.out.println("RPN: " + rpn);
        Tnode res = buildFromRPN(rpn);
        System.out.println("Tree: " + res);
        // TODO!!! Your tests here
    }
}
