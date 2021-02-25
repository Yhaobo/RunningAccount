package erp.util;

import java.util.*;

/**
 * 通用树形结构节点(包含数据处理方法)模型;
 * 使用: 直接继承即可
 *
 * @author Yhaobo
 * @since 2020/9/12
 */
public class TreeNodeModel {
    protected Object id;
    protected Object parentId;
    protected Collection<TreeNodeModel> children;

    /**
     * 将扁平一维数据处理为树形结构数据
     *
     * @param nodeCollection   一维数据集合
     * @param oneGradeParentId 第一层节点的parentId(一般建议为0)
     * @return 第一层节点的集合
     */
    public static Collection<TreeNodeModel> handle(Collection<? extends TreeNodeModel> nodeCollection, Object oneGradeParentId) {
        //第一层节点的集合
        List<TreeNodeModel> oneGradeList = new ArrayList<>();
        //键为parentId, 值为TreeNode集合
        Map<Object, List<TreeNodeModel>> parentIdToNodeMap = new HashMap<>(nodeCollection.size() >> 1);
        for (TreeNodeModel i : nodeCollection) {
            if (i.getParentId().equals(oneGradeParentId)) {
                oneGradeList.add(i);
            } else {
                List<TreeNodeModel> treeNodeModels = parentIdToNodeMap.get(i.getParentId());
                if (treeNodeModels == null) {
                    treeNodeModels = new ArrayList<>();
                    treeNodeModels.add(i);
                    parentIdToNodeMap.put(i.getParentId(), treeNodeModels);
                } else {
                    treeNodeModels.add(i);
                }
            }
        }
        //设置子节点
        setChildrenRecursion(oneGradeList, parentIdToNodeMap);

        return oneGradeList;
    }

    /**
     * 将扁平化数据处理为树形结构数据
     *
     * @param oneGradeCollection  第一层节点集合
     * @param elseGradeCollection 除第一层外的所有节点集合
     */
    public static void handle(Collection<? extends TreeNodeModel> oneGradeCollection, Collection<? extends TreeNodeModel> elseGradeCollection) {
        //键为parentId, 值为TreeNode集合
        Map<Object, List<TreeNodeModel>> parentIdToNodeMap = new HashMap<>(oneGradeCollection.size() << 1);
        for (TreeNodeModel i : elseGradeCollection) {
            List<TreeNodeModel> treeNodeModels = parentIdToNodeMap.get(i.getParentId());
            if (treeNodeModels == null) {
                treeNodeModels = new ArrayList<>();
                treeNodeModels.add(i);
                parentIdToNodeMap.put(i.getParentId(), treeNodeModels);
            } else {
                treeNodeModels.add(i);
            }
        }
        //设置子节点
        setChildrenRecursion(oneGradeCollection, parentIdToNodeMap);
    }

    private static void setChildrenRecursion(Collection<? extends TreeNodeModel> peerNodes, Map<Object, List<TreeNodeModel>> parentIdToModel) {
        for (TreeNodeModel node : peerNodes) {
            List<TreeNodeModel> treeNodeModels = parentIdToModel.get(node.getId());
            if (treeNodeModels == null) {
                return;
            } else {
                node.setChildrenRecursion(treeNodeModels);
                setChildrenRecursion(node.getChildren(), parentIdToModel);
            }
        }
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Collection<TreeNodeModel> getChildren() {
        return children;
    }

    public void setChildrenRecursion(Collection<TreeNodeModel> children) {
        this.children = children;
    }

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", children=" + children +
                '}';
    }
}
