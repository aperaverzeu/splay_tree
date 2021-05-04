public abstract class BSTree {

    public Node root;

    protected int size;

    protected abstract Node createNode(Node parent, Node left, Node right, Integer value);

    public Node search(Integer element) {
        Node node = root;
        while (node != null && node.value != null && !node.value.equals(element)) {
            node = element < node.getValue() ? node.left : node.right;
        }
        return node;
    }

    public Node insert(Integer element) {
        if (root == null) {
            root = createNode(null, null, null, element);
            size++;
            return root;
        }

        try {
            Node insertParentNode = null;
            Node searchTempNode = root;
            while (searchTempNode != null && searchTempNode.value != null) {
                insertParentNode = searchTempNode;
                searchTempNode = element < searchTempNode.value ? searchTempNode.left : searchTempNode.right;
            }

            Node newNode = createNode(insertParentNode, null, null, element);
            assert insertParentNode != null;
            if (insertParentNode.value > newNode.value) {
                insertParentNode.left = newNode;
            } else {
                insertParentNode.right = newNode;
            }

            size++;
            return newNode;
        } catch (Exception e) {
            throw new NullPointerException("it is forbiden to insert null element. Catched on " + size + "'th element");
        }
    }

    public Node delete(Integer element) {
        Node deleteNode = search(element);
        return deleteNode != null ? delete(deleteNode) : null;
    }


    protected Node delete(Node deleteNode) {
        if (deleteNode != null) {
            Node nodeToReturn;
            if (deleteNode.left == null) {
                nodeToReturn = transplant(deleteNode, deleteNode.right);
            } else if (deleteNode.right == null) {
                nodeToReturn = transplant(deleteNode, deleteNode.left);
            } else {
                Node successorNode = getMinimum(deleteNode.right);
                if (successorNode.parent != deleteNode) {
                    transplant(successorNode, successorNode.right);
                    successorNode.right = deleteNode.right;
                    successorNode.right.parent = successorNode;
                }
                transplant(deleteNode, successorNode);
                successorNode.left = deleteNode.left;
                successorNode.left.parent = successorNode;
                nodeToReturn = successorNode;
            }
            size--;

            return nodeToReturn;
        }
        return null;
    }

    // Put one node from tree (newNode) to the place of another (nodeToReplace).
    private Node transplant(Node nodeToReplace, Node newNode) {
        if (nodeToReplace.parent == null) {
            this.root = newNode;
        } else if (nodeToReplace == nodeToReplace.parent.left) {
            nodeToReplace.parent.left = newNode;
        } else {
            nodeToReplace.parent.right = newNode;
        }
        if (newNode != null) {
            newNode.parent = nodeToReplace.parent;
        }
        return newNode;
    }

    public boolean contains(Integer element) {
        return search(element) != null;
    }

    public int getMinimum() {
        return getMinimum(root).value;
    }

    public int getMaximum() {
        return getMaximum(root).value;
    }

    public int getSuccessor(Integer element) {
        return getSuccessor(search(element)).value;
    }

    public int getSize() {
        return size;
    }

    public void printTreeInOrder() {
        printTreeInOrder(root);
    }

    public void printTreePreOrder() {
        printTreePreOrder(root);
    }

    public void printTreePostOrder() {
        printTreePostOrder(root);
    }

    private void printTreeInOrder(Node entry) {
        if (entry != null) {
            printTreeInOrder(entry.left);
            if (entry.value != null) {
                System.out.println(entry.value);
            }
            printTreeInOrder(entry.right);
        }
    }

    private void printTreePreOrder(Node entry) {
        if (entry != null) {
            if (entry.value != null) {
                System.out.println(entry.value);
            }
            printTreeInOrder(entry.left);
            printTreeInOrder(entry.right);
        }
    }

    private void printTreePostOrder(Node entry) {
        if (entry != null) {
            printTreeInOrder(entry.left);
            printTreeInOrder(entry.right);
            if (entry.value != null) {
                System.out.println(entry.value);
            }
        }
    }

    protected Node getMinimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    protected Node getMaximum(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    protected Node getSuccessor(Node node) {
//        if (node == null) {
//            return null;
//        }
        // if there is right branch, then successor is leftmost node of that
        // subtree
        if (node.right != null) {
            return getMinimum(node.right);
        } else { // otherwise it is a lowest ancestor whose left child is also
            // ancestor of node
            Node currentNode = node;
            Node parentNode = node.parent;
            while (parentNode != null && currentNode == parentNode.right) {
                // go up until we find parent that currentNode is not in right
                // subtree.
                currentNode = parentNode;
                parentNode = parentNode.parent;
            }
            return parentNode;
        }
    }

    //-------------------------------- TREE PRINTING ------------------------------------

    public void printTree() {
        printSubtree(root);
    }

    public void printSubtree(Node node) {
        if (node.right != null) {
            printTree(node.right, true, "");
        }
        printNodeValue(node);
        if (node.left != null) {
            printTree(node.left, false, "");
        }
    }

    private void printNodeValue(Node node) {
        if (node.value == null) {
            System.out.print("<null>");
        } else {
            System.out.print(node.value);
        }
        System.out.println();
    }

    private void printTree(Node node, boolean isRight, String indent) {
        if (node.right != null) {
            printTree(node.right, true, indent + (isRight ? "        " : " |      "));
        }
        System.out.print(indent);
        if (isRight) {
            System.out.print(" /");
        } else {
            System.out.print(" \\");
        }
        System.out.print("----- ");
        printNodeValue(node);
        if (node.left != null) {
            printTree(node.left, false, indent + (isRight ? " |      " : "        "));
        }
    }


    protected void rotateLeft(Node node) {
        Node temp = node.right;
        temp.parent = node.parent;

        node.right = temp.left;
        if (node.right != null) {
            node.right.parent = node;
        }

        temp.left = node;
        node.parent = temp;

        // temp took over node's place so now its parent should point to temp
        if (temp.parent != null) {
            if (node == temp.parent.left) {
                temp.parent.left = temp;
            } else {
                temp.parent.right = temp;
            }
        } else {
            root = temp;
        }

    }

    protected void rotateRight(Node node) {
        Node temp = node.left;
        temp.parent = node.parent;

        node.left = temp.right;
        if (node.left != null) {
            node.left.parent = node;
        }

        temp.right = node;
        node.parent = temp;

        // temp took over node's place so now its parent should point to temp
        if (temp.parent != null) {
            if (node == temp.parent.left) {
                temp.parent.left = temp;
            } else {
                temp.parent.right = temp;
            }
        } else {
            root = temp;
        }
    }


    static class Node {
        public Node(Node parent, Node left, Node right, Integer value) {
//            super();
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.value = value;
        }

        public Integer value;
        public Node parent;
        public Node left;
        public Node right;

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            if (value == null) {
                return other.value == null;
            } else return value.equals(other.value);
        }

        @Override
        public String toString() {
            return String.valueOf(value == null ? "why is null?!" : value);
        }
    }
}
