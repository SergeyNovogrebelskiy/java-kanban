package history;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
public class CustomLinkedList<T> {

    class Node<Task> {
        public Task data;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Node<Task> prev, Task data, Node<Task> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    Node<Task> head;

    Node<Task> tail;

    HashMap<Integer, Node> tasks = new HashMap<>();
    private int STORY_SIZE = 10;

    public void setSTORY_SIZE(int STORY_SIZE) {
        this.STORY_SIZE = STORY_SIZE;
    }

    public Task getFirst() {
        final Node<Task> curHead = head;
        if (curHead == null)
            throw new NoSuchElementException();
        return head.data;
    }

    public void addLast(Task task) {
        if (checkTaskIsOnList(task.getId())) {
            removeTask(task);
        } else {
            if (tasks.size() > STORY_SIZE - 1) {
                int idHead = head.data.getId();
                removeTask(head.data);
                tasks.remove(idHead);
            }
        }
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        tasks.put(task.getId(), newNode);
    }

    public void removeTask (Task task) {
        Node<Task> node = tasks.get (task.getId());
        if (head.equals(node)) {
            if (tasks.size() != 1) {
                Node<Task> next = node.next;
                next.prev = null;
                head = next;
            }
        } else if (tail.equals(node)) {
            Node<Task> prev = node.prev;
            prev.next = null;
            tail = prev;
        } else {
            Node<Task> prev = node.prev;
            Node<Task> next = node.next;
            prev.next = next;
            next.prev = prev;
        }
    }

    public boolean checkTaskIsOnList (int id) {
        return tasks.containsKey(id);
    }

    public Task getLast () {
        final Node<Task> curHead = tail;
        if (curHead == null)
            throw new NoSuchElementException();
        return tail.data;
    }

    public List<Task> getHistoryTasks () {
        List<Task> res = new ArrayList<>();
        Node<Task> node = tail;
        while (node!=null) {
            res.add(node.data);
            node = node.prev;
        }
        return res;
    }

    public void remove (int id) {
        if (checkTaskIsOnList(id)) {
            Node<Task> node = tasks.get(id);
            removeTask(node.data);
        }
    }
}
