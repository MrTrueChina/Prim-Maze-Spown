package maze;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

public class MazeSpowner {
    private static LinkedList<Point> _readyNodes;
    private static LinkedList<Point> _carvedNodes;
    private static Maze _maze;

    private enum NodeState {
        DEACTIVE, READY_CARVE, CARVED,
    }

    private static NodeState[][] _nodes;

    private MazeSpowner() {
    }

    public static Maze spown(final int width, final int height) {
        try {
            setupSpowner(width, height);

            doSpown();

            return _maze;
        } finally {
            cleanSpowner();
        }
    }

    private static void setupSpowner(final int width, final int height) {
        int mazeWidth = width % 2 != 0 ? width : width + 1;
        int mazeHeight = height % 2 != 0 ? height : height + 1;

        _maze = new Maze(mazeWidth, mazeHeight);
        _readyNodes = new LinkedList<Point>();
        _carvedNodes = new LinkedList<Point>();

        _nodes = new NodeState[mazeWidth / 2][mazeHeight / 2];
        for (int y = 0; y < _nodes[0].length; y++)
            for (int x = 0; x < _nodes.length; x++)
                _nodes[x][y] = NodeState.DEACTIVE;
    }

    private static void cleanSpowner() {
        _maze = null;
        _readyNodes = null;
        _carvedNodes = null;
        _nodes = null;
    }

    private static void doSpown() {
        long startSetupMazeTime = System.currentTimeMillis();
        setupMaze();
        System.out.println("��ʼ���Թ���ʱ " + (System.currentTimeMillis() - startSetupMazeTime) + " ����");

        long startCarveMazeTime = System.currentTimeMillis();
        carveMaze();
        System.out.println("����Թ���ʱ " + (System.currentTimeMillis() - startCarveMazeTime) + " ����");
    }

    private static void setupMaze() {
        for (int x = 1; x < _maze.width; x++)
            for (int y = 1; y < _maze.height; y++)
                _maze.setPassable(false, x, y);

        for (int x = 1; x < _maze.width; x += 2)
            for (int y = 1; y < _maze.height; y += 2)
                _maze.setPassable(true, x, y);
    }

    private static void carveMaze() {
        long startOpenStartNodeTime = System.currentTimeMillis();
        carveStartNode();
        System.out.println("������ʱ " + (System.currentTimeMillis() - startOpenStartNodeTime) + " ����");

        while (_readyNodes.size() > 0) {
            long startCarveNodeTime = System.currentTimeMillis();
            carveRandomNode();
            System.out.println("���һ���ڵ��ʱ " + (System.currentTimeMillis() - startCarveNodeTime) + " ����");
        }
    }

    private static void carveRandomNode() {
        /*
         * ���һ���ڵ�
         * 
         * 0����������ķ�㷨��ÿ���ڿ�������һ��������ıձ���������ĵ㣬���������е��ǵȾ�ģ������������һ������ĵ������ӵ����һ�����ڵ�
         * 1���ڿ���������ҳ�һ����
         * 2������������Χ�ҳ������ڱձ���ĵ�
         * 3������Щ���������һ����ͨ
         * 4����������ձ�
         * 5���������Χ�ĵ���뿪��
         */
        Point readyNode = getRandomReadyNode();
        Point carvedNode = getRandomContiguousCarvedNode(readyNode);

        carved(carvedNode, readyNode);
        _readyNodes.remove(readyNode);
        _carvedNodes.add(readyNode);
    }

    private static void carveStartNode() {
        /*
         * ����
         * 
         * 1����һ��û�е�̵ĵ�
         * 2����������ձ���Ϊ�ѵ�̵ĵ������
         * 3����������ڵĵ���뿪��
         */
        Point startNode = getUncarvedNode();
        _carvedNodes.add(startNode);
        if (_maze.contains(startNode.x, startNode.y + 2))
            _readyNodes.add(new Point(startNode.x, startNode.y + 2));
        if (_maze.contains(startNode.x + 2, startNode.y))
            _readyNodes.add(new Point(startNode.x + 2, startNode.y));
        if (_maze.contains(startNode.x, startNode.y - 2))
            _readyNodes.add(new Point(startNode.x, startNode.y - 2));
        if (_maze.contains(startNode.x - 2, startNode.y))
            _readyNodes.add(new Point(startNode.x - 2, startNode.y));
    }

    private static Point getUncarvedNode() {
        for (int y = 0; y < _maze.height; y++)
            for (int x = 0; x < _maze.width; x++)
                if (_maze.getPassable(x, y))
                    return new Point(x, y);
        return null;
    }

    private static Point getRandomReadyNode() {
        return getRandomNode(_readyNodes);
    }

    /**
     * �����ȡһ��ָ���ڵ����ڵ��Ѿ���̵Ľڵ�
     * 
     * @param centerPoint Ҫ��ȡ���ڽڵ�Ľڵ�
     * @return ����������ڵ��ѵ�̽ڵ㣬�򷵻�һ���ѵ�̽ڵ㣬���򷵻�null
     */
    private static Point getRandomContiguousCarvedNode(final Point centerPoint) {
        LinkedList<Point> nodes = getContiguousNodes(centerPoint);

        Iterator<Point> nodesIterator = nodes.iterator();
        while (nodesIterator.hasNext())
            if (!_carvedNodes.contains(nodesIterator.next()))
                nodesIterator.remove(); // ͨ���������Ƴ�Ԫ�أ�������������� next() ֮����ã�����ֻ���Ƴ��ո�ͨ�� next() ��ȡ��Ԫ��

        if (nodes.size() > 0)
            return getRandomNode(nodes);
        return null;
    }

    private static Point getRandomNode(LinkedList<Point> nodes) {
        if (nodes.size() != 0)
            return nodes.get(random(nodes.size()));
        return null;
    }

    private static int random(int bound) {
        return (int) (Math.random() * bound);
    }

    private static LinkedList<Point> getContiguousNodes(final Point centerPoint) {
        LinkedList<Point> nodes = new LinkedList<Point>();

        if (_maze.contains(centerPoint.x, centerPoint.y + 2))
            nodes.add(new Point(centerPoint.x, centerPoint.y + 2));
        if (_maze.contains(centerPoint.x + 2, centerPoint.y))
            nodes.add(new Point(centerPoint.x + 2, centerPoint.y));
        if (_maze.contains(centerPoint.x, centerPoint.y - 2))
            nodes.add(new Point(centerPoint.x, centerPoint.y - 2));
        if (_maze.contains(centerPoint.x - 2, centerPoint.y))
            nodes.add(new Point(centerPoint.x - 2, centerPoint.y));

        return nodes;
    }

    private static void carved(Point startPoint, Point targetPoint) {
        _maze.setPassable(true, (startPoint.x + targetPoint.x) / 2, (startPoint.y + targetPoint.y) / 2);

        LinkedList<Point> contiguousNode = getContiguousNodes(targetPoint);

        for (Point point : contiguousNode)
            if (!_readyNodes.contains(point) && !_carvedNodes.contains(point))
                _readyNodes.add(point);
    }
}
