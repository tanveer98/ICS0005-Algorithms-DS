import java.util.*;

/**
 * Container class to different classes, that makes the whole set of classes one
 * class formally.
 */
public class GraphTask {

   /** Main method. */
   public static void main(String[] args) {
      GraphTask a = new GraphTask();
      try {
         a.run();
      } catch (Exception e) {
         e.printStackTrace();
      }
      // throw new RuntimeException ("Nothing implemented yet!"); // delete this
   }

   /** Actual main method to run examples and everything. */

   private void test_assertion(Graph g1, Graph g2, boolean cond) {
      String res = null;
      System.out.println("\t*******Begin assertion*******");
      res = (g1.isCloneOf(g2) == cond) ?  "" : "Assertion failed!!" ;
      
      System.out.println(res);
      System.out.println("assert G1 == G2: " + cond);
      System.out.println(g1);
      System.out.println(g2);
      if(res.isEmpty() == false) {
         System.exit(77);
      }
   }
   
   public void run() throws CloneNotSupportedException {

      // Test plan:
      //// 1) Create a simple tree
      //// 2) Create random graph (with edgecount == node count)
      //// 3) Create random graph(edgecount > node count)
      //// 4) Create simple graph with loops.
      //// 5) Createmultigraph (multiple bidrectional arcs to few nodes)
      //// 6) Createmultigraphwith loops
      //// 7) create graph with one node and 1 or more loops.

      Graph g1 = new Graph("G");
      Graph g2 = null;
      boolean args[][] = {
         {false,false}, // no loops no multigraph
         {true, false}, // loops but no multigraph
         {false, true}, // multigraph without loops
         {true,true}, //multigraph with loops
      };


      // test 1
      g1.createRandomTree(3);
      g2 = (Graph) g1.clone();
      test_assertion(g1, g2, true);

      g1.createVertex("VX");
      test_assertion(g1, g1, true);
      test_assertion(g1, g2, false);

      g2.createRandomTree(3);
      test_assertion(g1, g2, false);
      g1.clearGraph();
      g2.clearGraph();


      //test 2
      g1.createRandomGraph(5,10,false,false);
      g2 = (Graph) g1.clone();
      test_assertion(g1, g2, true);

      g1.createVertex("V_NEW");
      g1.createArc("A_NEW", g1.first.next, g1.first);
      test_assertion(g1, g1, true);
      test_assertion(g1, g2, false);
      g1.clearGraph();
      g2.clearGraph();;

    
      // different number of arcs and edges
      for(int i = 0; i < args.length; i++) {
         int node = 5;
         int edges = 9;
         g1.createRandomGraph(node,edges,args[i][0],args[i][1]);
         g2 = (Graph) g1.clone();
         test_assertion(g1, g2, true);

         g1.createVertex("V_NEW");
         g1.createArc("A_NEW", g1.first, g1.first.next);

         g1.clearGraph();
         g2.clearGraph();
      }

      //graph with one node and multiple loops
      g1.createGraphWithOneNode(5);
      g2 = (Graph) g1.clone();
      test_assertion(g1, g2, true);
      g2.createGraphWithOneNode(1);
      test_assertion(g1, g2, false);
      g1.clearGraph();
      test_assertion(g1, g2, false);
      g2.clearGraph();
      
      test_assertion(g1, g2, true);
      
   }

   class Vertex {

      private String id;
      private Vertex next; // ref to the first `Vertex` in a (linked)List of vertex
      private Arc first;   // ref to the first `Arc` in a (linked)List of arcs, Each arc in the list
                           // contains a `target` vertex, meaning this(vertex) and target are connected.
      private int info = 0;
      // You can add more fields, if needed

      Vertex(String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex(String s) {
         this(s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

      public boolean equals(Object obj) {
         // checks if this vertex and param vertex have the same id and same arcs.
         Vertex v = (Vertex) obj;
         String thisID = this.id;
         String rhsID = v.id;

         if (thisID.equals(rhsID)) {
            // id's match, now check if arcs match or not
            Arc thisArc = this.first;
            Arc rhsArc = v.first;
            
            if(thisArc == null) {
               if(rhsArc == null) {
                  return true;
               }
               else {
                  return false;
               }
            }
            else {
            return thisArc.isClone(rhsArc);
            }
         }
         return false;
      }

      /**
       * Checks if a list of vertex is equal or not
       * 
       * @param Vertex list to compare
       * @return true if they are equal, false if not.
       */
      public boolean isClone(Vertex obj) {
         Vertex rhs = obj;
         Vertex thisVertex = this;

         while (thisVertex != null && rhs != null) {
            if (!thisVertex.equals(rhs)) {
               return false;
            }
            thisVertex = thisVertex.next;
            rhs = rhs.next;
         }
         // both thisArc and rhs should be null at this point
         return thisVertex == rhs;
      }
   }

   /**
    * Arc represents one arrow in the graph. Two-directional edges are represented
    * by two Arc objects (for both directions).
    */
   class Arc {

      private String id;
      private Vertex target;
      private Arc next;
      private int info = 0;
      // You can add more fields, if needed

      Arc(String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      Arc(String s) {
         this(s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

      // TODO!!! Your Arc methods here!
      @Override
      public boolean equals(Object obj) {
         Arc rhs = (Arc) obj;
         String thisID = this.id;
         String rhsID = rhs.id;

         return thisID.equals(rhsID);
      }

      /**
       * Compares A linkedList of arc with another linkedeList of arc
       */
      public boolean isClone(Arc obj) {
         Arc thisArc = this;
         Arc rhs = obj;

         while (thisArc != null && rhs != null) {
            if (!thisArc.equals(rhs)) {
               return false;
            }
            thisArc = thisArc.next;
            rhs = rhs.next;
         }
         // both thisArc and rhs should be null at this point
         return thisArc == rhs;
      }
   }

   class Graph {

      private String id;
      private Vertex first;
      private int info = 0;
      // You can add more fields, if needed

      Graph(String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph(String s) {
         this(s, null);
      }

      @Override
      public String toString() {
         String nl = System.getProperty("line.separator");
         StringBuffer sb = new StringBuffer(nl);
         sb.append(id);
         sb.append(nl);
         Vertex v = first;
         while (v != null) {
            sb.append(v.toString());
            sb.append(" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append(" ");
               sb.append(a.toString());
               sb.append(" (");
               sb.append(v.toString());
               sb.append("->");
               sb.append(a.target.toString());
               sb.append(")");
               a = a.next;
            }
            sb.append(nl);
            v = v.next;
         }
         return sb.toString();
      }

      /**
       * Adds vertex with id = `vid` to the (linked)List of vertex(es)
       * 
       * @param vid
       * @return
       */
      public Vertex createVertex(String vid) {
         Vertex res = new Vertex(vid);
         res.next = first;
         first = res;
         return res;
      }

      /**
       * Function to create an `Arc` (which represents a directed edge) between `from`
       * and `to`
       * 
       * @param aid
       * @param from
       * @param to
       * @return
       */
      public Arc createArc(String aid, Vertex from, Vertex to) {
         Arc res = new Arc(aid);
         // push new Arc `res` into Vertex `from` linkedList
         res.next = from.first;
         from.first = res;
         // res.target = destination vertex
         res.target = to;
         return res;

      }

      /**
       * Create a connected undirected random tree with n vertices. Each new vertex is
       * connected to some random existing vertex.
       * 
       * @param n number of vertices added to this graph
       */
      public void createRandomTree(int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex[n];
         for (int i = 0; i < n; i++) {
            // "Push" new vertex to the linkedList of vertexes.
            varray[i] = createVertex("v" + String.valueOf(n - i));
            if (i > 0) {
               // Find an existing vertex between 0 and i
               int vnr = (int) (Math.random() * i);
               // Create (directed) arc from (existing) random vertex to i
               createArc("a" + varray[vnr].toString() + "_" + varray[i].toString(), varray[vnr], varray[i]);
               // And in reverse
               createArc("a" + varray[i].toString() + "_" + varray[vnr].toString(), varray[i], varray[vnr]);
            } else {
            }
         }
      }

      /**
       * Create an adjacency matrix of this graph. Side effect: corrupts info fields
       * in the graph
       * 
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int[info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res[i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Create a connected simple (undirected, no loops, no multiple arcs) random
       * graph with n vertices and m edges.
       * 
       * @param n number of vertices
       * @param m number of edges
       */
      public void createRandomGraph(int n, int m, boolean withLoop, boolean isMultigraph) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException("Too many vertices: " + n);
         if (m < n - 1 || m > n * (n - 1) / 2)
            throw new IllegalArgumentException("Impossible number of edges: " + m);
         first = null;
         createRandomTree(n); // n-1 edges created here
         Vertex[] vert = new Vertex[n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1; // remaining edges
         while (edgeCount > 0) {
            int i = (int) (Math.random() * n); // random source
            int j = (int) (Math.random() * n); // random target
            if (i == j && !withLoop)
               continue; // no loops
            if ((connected[i][j] != 0 || connected[j][i] != 0) && !isMultigraph )
               continue; // no multiple edges
            Vertex vi = vert[i];
            Vertex vj = vert[j];
            createArc("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected[i][j] = 1;
            createArc("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected[j][i] = 1;
            edgeCount--; // a new edge happily created
         }
      }

      /**
       * creates graph with only one node and 1 or more loops
       * @param n number of loops
       */
      public void createGraphWithOneNode(int n) {
         // vertex already created
         while(n != 0) {
            createArc("av1_v1", this.first, this.first);
            n--;
         }
      }

      // TODO!!! Your Graph methods here! Probably your solution belongs here.

      /**
       * Creates a clone (deep copy) of this graph
       * @return Graph object
       */
      @Override
      public Object clone() throws CloneNotSupportedException {
         Graph cln = null;
         Vertex thisVertex = null;
         Vertex cloneVertex = null;

         ArrayDeque<Vertex> vertexStack = null;
         ArrayDeque<Vertex> vStackCpy = null;
         // map will contains NEW copies of vertices along with their id.
         Map<String, Vertex> cloneVertexMap = null;

         // init datastrcutures
         cln = new Graph(this.id);
         cloneVertexMap = new HashMap<>();
         vertexStack = new ArrayDeque<>();
         thisVertex = this.first;

         // list all vertices of the graph
         while (thisVertex != null) {
            vertexStack.push(thisVertex);
            thisVertex = thisVertex.next;
         }

         try {
            vStackCpy = new ArrayDeque<>(vertexStack);
         } catch (Exception e) {
            e.printStackTrace();
         }

         // insert copies of vertices in the graph
         while (vStackCpy.isEmpty() == false) {
            Vertex n = null;
            String id = null;
            thisVertex = vStackCpy.pop();
            id = thisVertex.id;
            n = cln.createVertex(id);
            cloneVertexMap.put(id, n);
         }

         try {
            vStackCpy = new ArrayDeque<>(vertexStack);
         } catch (Exception e) {
            e.printStackTrace();
         }

         // now the graph contains copy of all the vertices of the original.
         // now its time to copy arcs.
         while (vStackCpy.isEmpty() == false) {
            // Arc arc = null;
            Arc thisArc = null;
            Deque<Arc> arcStack = new ArrayDeque<>();

            // init
            thisVertex = vStackCpy.pop();
            thisArc = thisVertex.first;
            cloneVertex = cloneVertexMap.get(thisVertex.id);

            // insert all arcs into stack
            while (thisArc != null) {
               arcStack.push(thisArc);
               thisArc = thisArc.next;
            }

            // create arcs
            while (arcStack.isEmpty() == false) {
               String aid = null;
               Vertex from = null;
               Vertex to = null;

               // initalize
               thisArc = arcStack.pop();
               aid = thisArc.id;
               from = cloneVertex;
               to = cloneVertexMap.get(thisArc.target.id);

               cln.createArc(aid, from, to);
            }

         }
         return cln;
      }

      /**
       * Checks if 2 graphs are clones or not
       * 
       * @param obj
       * @return
       */
      public boolean isCloneOf(Graph obj) {
         Graph rhs = obj;
         Vertex thisVertex = this.first;
         Vertex rhsVertex = rhs.first;

         return thisVertex.isClone(rhsVertex);

      }
      
      /**
       * Deletes the existing graph by assigining the graph.first to a new (empty) vertex
       */
      public void clearGraph() {
         this.first = new Vertex("V1");
         this.info = 0;
      }

   }
}
