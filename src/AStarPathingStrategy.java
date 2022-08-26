import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{
    private Double hCost(Point cur, Point end)
    {
        return Math.sqrt((cur.x-end.x)*(cur.x-end.x) + (cur.y-end.y)*(cur.y-end.y));
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Stack<Point> path = new Stack<>();
        /*define closed list
          define open list
          while (true){
            Filtered list containing neighbors you can actually move to
            Check if any of the neighbors are beside the target
            set the g, h, f values
            add them to open list if not in open list
            add the selected node to close list
          return path*/

        List<Point> openList = new ArrayList<>();
        List<Point> closeList = new ArrayList<>();

        Map<Point, Double> g = new HashMap<>();
        Map<Point, Double> f = new HashMap<>();
        Map<Point, Double> h = new HashMap<>();
        Map<Point, Point> trace = new HashMap<>();

        openList.add(start);
        f.put(start, 0.0);
        g.put(start, 0.0);

        while (!openList.isEmpty())
        {
            Double min = Double.MAX_VALUE;
            Point cur = null;
            for (Point p : openList)
                if (!closeList.contains(p) && Double.compare(f.get(p), min) < 0)
                {
                    min = f.get(p);
                    cur = p;
                }

            if (cur == null) return path;

            closeList.add(cur);
            openList.remove(cur);

            if(cur.equals(end))
                break;

            if (withinReach.test(cur, end))
            {
                trace.put(end, cur);
                closeList.add(end);
                break;
            }

            List<Point> neighbors = potentialNeighbors.apply(cur).filter(canPassThrough).toList();
            Double startToCur = g.get(cur);

            for (Point p : neighbors)
                if (!closeList.contains(p))
                {
                    if (h.get(p) == null)
                        h.put(p, hCost(p, end));

                    if (f.get(p) == null || f.get(p) > startToCur + h.get(p) + 1.0)
                    {
                        g.put(p, startToCur + 1.0);
                        f.put(p, startToCur + h.get(p) + 1.0);
                        trace.put(p, cur);
                        if (!openList.contains(p))
                            openList.add(p);
                    }
                }
        }

        if(closeList.contains(end))
        {
            Point cur = end;
            while(!cur.equals(start))
            {
                path.push(cur);
                cur = trace.get(cur);
            }
        }

        return path;
    }
}
