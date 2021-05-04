import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class Splay extends BSTree implements Iterable<Integer> {

    @Override
    protected Node createNode(Node parent, Node left, Node right, Integer value) {
        return new Node(parent, left, right, value);
    }


    @Override
    public Node search(Integer element) {
        Node node = super.search(element);
        if (node != null) {
            splay(node);
        }
        return node;
    }


    @Override
    public Node insert(Integer element) {
        Node insertNode = super.insert(element);
        splay(insertNode);
        return insertNode;
    }


    @Override
    public Node delete(Integer element) {
        Node deleteNode = super.search(element);
        Node successor = null;
        if (deleteNode != null) {
            Node parent = deleteNode.parent;
            successor = super.delete(deleteNode);
            if (parent != null) {
                splay(parent);
            }
        }
        return successor;
    }

    protected void splay(Node node) {
        // move node up until its root
        while (node != root) {
            // Zig step
            Node parent = node.parent;
            if (parent.equals(root)) {
                if (node.equals(parent.left)) {
                    rotateRight(parent);
                } else if (node.equals(parent.right)) {
                    rotateLeft(parent);
                }
                break;
            } else {
                Node grandParent = parent.parent;
                boolean nodeAndParentLeftChildren = node.equals(parent.left) && parent.equals(grandParent.left);
                boolean nodeAndParentRightChildren = node.equals(parent.right) && parent.equals(grandParent.right);
                boolean nodeRightChildParentLeftChild = node.equals(parent.right) && parent.equals(grandParent.left);
                boolean nodeLeftChildParentRightChild = node.equals(parent.left) && parent.equals(grandParent.right);
                // Zig zig step to the right
                if (nodeAndParentLeftChildren) {
                    rotateRight(grandParent);
                    rotateRight(parent);
                }
                // Zig zig step to the left
                else if (nodeAndParentRightChildren) {
                    rotateLeft(grandParent);
                    rotateLeft(parent);
                }
                // Zig zag steps
                else if (nodeRightChildParentLeftChild) {
                    rotateLeft(parent);
                    rotateRight(grandParent);
                }
                else if (nodeLeftChildParentRightChild) {
                    rotateRight(parent);
                    rotateLeft(grandParent);
                }
            }
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new SplayIterator(this.root);
    }

    public static class SplayIterator implements Iterator<Integer> {
        Stack<Node> nodes;

        public SplayIterator(Node root) {
            nodes = new Stack<>();
            while (root != null) {
                nodes.push(root);
                root = root.left;
            }
        }

        public boolean hasNext() {
            return !nodes.isEmpty();
        }

        public Integer next() {
            if(!hasNext()) throw new NoSuchElementException();

            Node node = nodes.pop();
            Integer value = node.value;
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    nodes.push(node);
                    node = node.left;
                }
            }
            return value;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
