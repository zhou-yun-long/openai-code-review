package plus.gaga.middleware.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 论文排版系统 - 提供论文格式化相关的核心功能
 */
public class PaperFormattingService {

    /**
     * 论文章节
     */
    public static class Section {
        private String title;
        private String content;
        private int level;

        public Section(String title, String content, int level) {
            this.title = title;
            this.content = content;
            this.level = level;
        }

        public String getTitle() { return title; }
        public String getContent() { return content; }
        public int getLevel() { return level; }
    }

    /**
     * 论文对象
     */
    public static class Paper {
        private String title;
        private String author;
        private String abstractText;
        private List<String> keywords;
        private List<Section> sections;
        private List<String> references;

        public Paper(String title, String author) {
            this.title = title;
            this.author = author;
            this.keywords = new ArrayList<>();
            this.sections = new ArrayList<>();
            this.references = new ArrayList<>();
        }

        public void setAbstract(String abstractText) {
            this.abstractText = abstractText;
        }

        public void addKeyword(String keyword) {
            keywords.add(keyword);
        }

        public void addSection(Section section) {
            sections.add(section);
        }

        public void addReference(String reference) {
            references.add(reference);
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getAbstractText() { return abstractText; }
        public List<String> getKeywords() { return keywords; }
        public List<Section> getSections() { return sections; }
        public List<String> getReferences() { return references; }
    }

    /**
     * 校验论文格式是否符合规范
     *
     * @param paper 待校验的论文对象
     * @return 校验通过返回 true，否则返回 false
     */
    public boolean validate(Paper paper) {
        if (paper == null) {
            return false;
        }
        if (paper.getTitle() == null || paper.getTitle().trim().isEmpty()) {
            return false;
        }
        if (paper.getAuthor() == null || paper.getAuthor().trim().isEmpty()) {
            return false;
        }
        if (paper.getAbstractText() == null || paper.getAbstractText().trim().isEmpty()) {
            return false;
        }
        if (paper.getKeywords() == null || paper.getKeywords().isEmpty()) {
            return false;
        }
        if (paper.getSections() == null || paper.getSections().isEmpty()) {
            return false;
        }
        return true;
    }

    private static String repeat(String str, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 生成论文目录
     *
     * @param paper 论文对象
     * @return 目录字符串
     */
    public String generateTableOfContents(Paper paper) {
        if (paper == null || paper.getSections() == null) {
            return "";
        }
        StringBuilder toc = new StringBuilder();
        toc.append("目录\n");
        toc.append("================\n");
        int pageNum = 1;
        for (Section section : paper.getSections()) {
            int indentLevel = Math.max(0, section.getLevel() - 1);
            String indent = repeat("  ", indentLevel);
            toc.append(indent).append(section.getTitle())
               .append(" ........ ").append(pageNum).append("\n");
            pageNum++;
        }
        return toc.toString();
    }

    /**
     * 格式化参考文献列表
     *
     * @param paper 论文对象
     * @return 格式化后的参考文献字符串
     */
    public String formatReferences(Paper paper) {
        if (paper == null || paper.getReferences() == null || paper.getReferences().isEmpty()) {
            return "";
        }
        StringBuilder refs = new StringBuilder();
        refs.append("参考文献\n");
        refs.append("================\n");
        for (int i = 0; i < paper.getReferences().size(); i++) {
            refs.append("[").append(i + 1).append("] ").append(paper.getReferences().get(i)).append("\n");
        }
        return refs.toString();
    }

    /**
     * 计算论文字符数（标题+摘要+各章节内容之和）
     *
     * @param paper 论文对象
     * @return 论文字符数
     */
    public int countCharacters(Paper paper) {
        if (paper == null) {
            return 0;
        }
        int count = 0;
        if (paper.getTitle() != null) {
            count += paper.getTitle().length();
        }
        if (paper.getAbstractText() != null) {
            count += paper.getAbstractText().length();
        }
        List<Section> sections = paper.getSections();
        if (sections != null) {
            for (Section section : sections) {
                if (section.getContent() != null) {
                    count += section.getContent().length();
                }
            }
        }
        return count;
    }
}
