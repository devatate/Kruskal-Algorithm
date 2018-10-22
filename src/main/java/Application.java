import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Application {

    class Edge{
        Double cost;
        Vertex vertex1;
        Vertex vertex2;

        public Edge(Double cost,Vertex vertex1,Vertex vertex2){
            this.cost = cost;
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
        }

        public String toString(){
            return ""+vertex1.id+" "+vertex2.id+" "+cost;
        }
    }

    class Vertex{
        Vertex unionParent;
        Integer size;
        Integer id;

        public Vertex(Integer id){
            this.unionParent = this;
            size = 1;
            this.id = id;
        }

        void addChild(Vertex vertex2){
            if(vertex2.findRoot().size>findRoot().size){
                findRoot().unionParent =vertex2.findRoot();

            }else if(findRoot().size>vertex2.findRoot().size){
                vertex2.findRoot().unionParent = findRoot();
            }else{
                vertex2.findRoot().unionParent = findRoot();
                findRoot().size++;
            }
        }

        Vertex findRoot(){
            if(unionParent.equals(this)){
                return this;
            }
            Vertex realParent = unionParent.findRoot();
            unionParent = realParent;
            return unionParent;
        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        new Application().solve();
    }

    void solve() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int nofVertex = Integer.parseInt(scanner.nextLine().trim());
        int nofEdge = Integer.parseInt(scanner.nextLine().trim());

        List<Edge> edgeList = new ArrayList<Edge>();
        Map<Integer, Vertex> map = new HashMap<Integer, Vertex>();
        for (int queriesRowItr = 0; queriesRowItr < nofEdge; queriesRowItr++) {
            String[] queriesRowItems = scanner.nextLine().split(" ");
            Vertex vertex1 = getOrCreateVertex(Integer.parseInt(queriesRowItems[0].trim()), map);
            Vertex vertex2 = getOrCreateVertex(Integer.parseInt(queriesRowItems[1].trim()), map);
            double cost = Double.parseDouble(queriesRowItems[2].trim());
            edgeList.add(new Edge(cost, vertex1, vertex2));
        }

        List<Edge> resultList = kruskalAlgorithm(nofVertex, edgeList);
        Double totalCost =0d;
        for (Edge edge : resultList) {
            bufferedWriter.write(edge.toString()+"\n");
            totalCost += edge.cost;
        }
        bufferedWriter.write(totalCost.toString());

        bufferedWriter.newLine();

        bufferedWriter.close();
    }

    private List<Edge> kruskalAlgorithm(int nofVertex, List<Edge> edgeList) {
        Collections.sort(edgeList, new Comparator<Edge>(){
            public int compare (Edge edge1, Edge edge2){
                return edge1.cost.compareTo(edge2.cost);
            }
        });
        List<Edge> resultList = new ArrayList<Edge>();

        for(Edge edge : edgeList){
            if(edge.vertex1.findRoot().equals(edge.vertex2.findRoot())){
                continue;
            }else {
                edge.vertex1.addChild(edge.vertex2);
                resultList.add(edge);
            }
            if(nofVertex == resultList.size()){
                break;
            }
        }
        return resultList;
    }

    private Vertex getOrCreateVertex(Integer id, Map<Integer, Vertex> map){
        Vertex v = map.get(id);
        if(v== null){
            v=new Vertex(id);
        }
        map.put(id,v);
        return v;
    }
}
