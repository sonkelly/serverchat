package tools;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import vn.game.room.Phong;
import vn.game.room.Room;
import vn.game.room.Zone;
import vn.game.room.ZoneManager;
import vn.game.session.ISession;

@SuppressWarnings("serial")
public class Monitor extends JFrame implements ActionListener{

	private ZoneManager managerZ;

	public Monitor() {
		super("JTreeobject frame");
		setSize(300, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void setManagerZ(ZoneManager managerZ) {
		this.managerZ = managerZ;
	}

	public DefaultMutableTreeNode makeTreeNode(NodeType t, DefaultMutableTreeNode parrent, String name, Object obj) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
		
		//Node node = new Node();
		//Hashtable<Long, Node> children = new Hashtable<Long, Node>();
		//node.setType(t);
		//node.setName(name);
		/*if(parrent != null)
			node.setParrent(parrent);*/
		switch (t) {
		case Root: {
			Enumeration<Zone> zones = this.managerZ.getZones();
			while (zones.hasMoreElements()) {
				Zone zone = zones.nextElement();
				DefaultMutableTreeNode child = makeTreeNode(NodeType.Game, node,
						zone.getZoneName(), zone);
				node.add(child);
				//children.put((long) zone.getZoneId(), child);
			}
			break;
		}
		case Game: {
			Zone zone = (Zone) obj;
			Collection<Phong> phongs = zone.phongValues();
			for (Phong ph : phongs) {
				int index = ph.id;
				int level = ph.level;
				String n = index + "-" + level;
				DefaultMutableTreeNode child = makeTreeNode(NodeType.Phong, node, n, ph);
				node.add(child);
				//children.put((long) index, child);
			}
			break;
		}
		case Phong: {
			Phong phong = (Phong) obj;
			Collection<Room> rooms = phong.getRooms();
			for (Room r : rooms) {
				String n = r.getName();
				DefaultMutableTreeNode child = makeTreeNode(NodeType.Table, node, n, r);
				//children.put((long) r.index, child);
				node.add(child);
			}
			break;
		}
		case Table: {
			Room table = (Room) obj;
			Enumeration<ISession> ses = table.getEnteringSession().elements();
			while (ses.hasMoreElements()) {
				ISession s = ses.nextElement();
				String n = s.getUserName();
				DefaultMutableTreeNode child = makeTreeNode(NodeType.User, node, n, s);
				node.add(child);
				//children.put(s.getUID(), child);
			}
			break;
		}
		case User: {
			// Leaf
			return node;
		}
		default:
			return node;
		}
		//node.setChildren(children);
		return node;
	}
	public void actionPerformed(ActionEvent a) {
		//frame.init();
		DefaultMutableTreeNode root = makeTreeNode(NodeType.Root, null, "Root", null);
		
		JTree tree = new JTree(root);
		if(frame1 != null) frame1.dispose();
		frame1 = new JFrame("Hello");
		frame1.setSize(200, 100);
		//frame1.add(tree);
		JScrollPane treeView = new JScrollPane(tree);
		frame1.getContentPane().add(treeView, BorderLayout.CENTER);
		frame1.setVisible(true);
	}

	Monitor frame;
	JFrame frame1;
	public void main() {
		frame  = new Monitor();
		JButton button = new JButton("Submit");
		frame.add(button);
		frame.setLayout(new FlowLayout());
		frame.setSize(200, 100);
		//frame.setVisible(true);
		button.addActionListener(this);
		//frame.init();
		frame.setVisible(true);

	}
}

enum NodeType {
	Phong, Game, Table, User, Root

}

class Node implements TreeNode {
	Node parrent;
	Hashtable<Long, Node> children;
	NodeType type;
	String name;

	public Node() {
	}

	public Node(String name, NodeType t, Node p) {
		this.parrent = p;
		this.name = name;
		this.type = t;
	}

	public String getName() {
		return name;
	}

	public NodeType getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChildren(Hashtable<Long, Node> children) {
		this.children = children;
	}

	public void setParrent(Node parrent) {
		this.parrent = parrent;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	@Override
	public boolean isLeaf() {
		if (this.children.size() == 0)
			return true;
		else
			return false;
	}

	@Override
	public TreeNode getParent() {
		return parrent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return 0;
	}

	@Override
	public int getChildCount() {
		return this.children.size();
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		try {
			return this.children.get(childIndex);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public Enumeration<Node> children() {
		try {
		return this.children.elements();
		}catch (Exception e) {
			return null;
		}
	}
}