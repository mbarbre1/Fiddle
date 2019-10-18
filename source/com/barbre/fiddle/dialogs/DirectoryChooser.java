package com.barbre.fiddle.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class DirectoryChooser extends JTree implements FileFilter, Comparator {
	private DefaultTreeModel model = null;

	/**
	 * Constructor for DirectoryChooser.
	 */
	public DirectoryChooser() {
		super();
		model = new DefaultTreeModel(createNodes(null));
		setModel(model);
		setRootVisible(false);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new Renderer());
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
//				if (e.getClickCount() == 2) {
					int selRow = getRowForLocation(e.getX(), e.getY());
					TreePath selPath = getPathForLocation(e.getX(), e.getY());
					if (selRow != -1) {
						myDoubleClick(selRow, selPath);
					}
//				}
			}
		});
	}

	public static File showDialog() {
		DirectoryChooser dc = new DirectoryChooser();
		dc.setMinimumSize(new Dimension(400,4000));
		dc.setPreferredSize(dc.getMinimumSize());
		JScrollPane p = new JScrollPane();
		p.setViewportView(dc);
		p.setPreferredSize(new Dimension(400,400));
		int answer = JOptionPane.showConfirmDialog(null, p, "Locate your Everquest home directory", JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.CANCEL_OPTION)
			return null;
		return (File) ((DefaultMutableTreeNode) dc.getSelectionPath().getLastPathComponent()).getUserObject();
	}

	/**
	 * Method myDoubleClick.
	 * @param row
	 * @param path
	 */
	private void myDoubleClick(int row, TreePath path) {
		DefaultMutableTreeNode n = (DefaultMutableTreeNode) path.getLastPathComponent();
		if (!isExpanded(row)) {
			File f = (File) n.getUserObject();
			File[] dirs = getDirectoryFor(f);
			if (dirs != null) {
				for (int i = 0; i < dirs.length; i++) {
					n.add(createNodes(dirs[i]));
				}			
			}
			expandPath(path);			
		}
	}

	/**
	 * Method createNodes.
	 * @param path
	 * @return DefaultMutableTreeNode
	 */
	private DefaultMutableTreeNode createNodes(File path) {
		DefaultMutableTreeNode node = null;
		File[] dirs = null;
		if (path == null) {
			node = new DefaultMutableTreeNode();
			dirs = File.listRoots();
		} else {
			node = new DefaultMutableTreeNode(path);
			dirs = getDirectoryFor(path);
		}
		for (int i = 0; i < dirs.length; i++) {
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(dirs[i]);
			node.add(n);
		}
		return node;
	}

	/**
	 * Method getDirectoryFor.
	 * @param path
	 * @return File[]
	 */
	private File[] getDirectoryFor(File path) {
		File[] dirs;
		dirs = path.listFiles(this);
		if (dirs != null)
			Arrays.sort(dirs);
		return dirs;
	}

	/**
	 * @see java.io.FileFilter#accept(File)
	 */
	public boolean accept(File pathname) {
		return pathname.isDirectory();
	}

	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) {
		String s1 = ("" + o1).toLowerCase();
		String s2 = ("" + o2).toLowerCase();
		return s1.compareTo(s2);
	}


	/**
	 * Method main.
	 * @param args
	 */
	public static void main(String[] args) {
		File f = showDialog();
		System.out.println(f);
		System.exit(0);
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * Renderer
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	private class Renderer extends DefaultTreeCellRenderer {

		public Renderer() {
			super();
			setLeafIcon(getClosedIcon());
		}

		/**
		 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(JTree, Object, boolean, boolean, boolean, int, boolean)
		 */
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			try {
				DefaultMutableTreeNode n = (DefaultMutableTreeNode) value;
				File f = (File) n.getUserObject();
				if (f.getName().length() > 0)
					label.setText(f.getName());
					
			} catch(NullPointerException e) {
				label.setText("null");
			}
			return label;
			
		}

	}
	


}
