package plus.gaga.middleware.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import plus.gaga.middleware.domain.PaperFormattingService;
import plus.gaga.middleware.domain.PaperFormattingService.Paper;
import plus.gaga.middleware.domain.PaperFormattingService.Section;

/**
 * 论文排版系统测试
 */
public class PaperFormattingTest {

    private PaperFormattingService service;
    private Paper paper;

    @Before
    public void setUp() {
        service = new PaperFormattingService();

        paper = new Paper("基于深度学习的图像识别研究", "张三");
        paper.setAbstract("本文研究了基于深度学习的图像识别方法，提出了一种改进的卷积神经网络模型，" +
                "在多个标准数据集上取得了优异的性能。");
        paper.addKeyword("深度学习");
        paper.addKeyword("图像识别");
        paper.addKeyword("卷积神经网络");
        paper.addSection(new Section("引言", "随着人工智能技术的飞速发展...", 1));
        paper.addSection(new Section("相关工作", "近年来，深度学习在图像识别领域...", 1));
        paper.addSection(new Section("方法", "本文提出的方法...", 1));
        paper.addSection(new Section("实验结果", "在CIFAR-10数据集上...", 1));
        paper.addSection(new Section("结论", "本文提出了一种改进的卷积神经网络...", 1));
        paper.addReference("LeCun Y, et al. Gradient-based learning applied to document recognition[J]. " +
                "Proceedings of the IEEE, 1998, 86(11): 2278-2324.");
        paper.addReference("He K, et al. Deep residual learning for image recognition[C]. CVPR, 2016.");
    }

    @Test
    public void testValidatePaperWithAllFields() {
        Assert.assertTrue("完整论文应通过校验", service.validate(paper));
    }

    @Test
    public void testValidatePaperWithNullTitle() {
        Paper invalidPaper = new Paper(null, "张三");
        invalidPaper.setAbstract("摘要内容");
        invalidPaper.addKeyword("关键词");
        invalidPaper.addSection(new Section("第一章", "内容", 1));
        Assert.assertFalse("缺少标题的论文应校验失败", service.validate(invalidPaper));
    }

    @Test
    public void testValidatePaperWithEmptyAbstract() {
        Paper invalidPaper = new Paper("标题", "作者");
        invalidPaper.setAbstract("");
        invalidPaper.addKeyword("关键词");
        invalidPaper.addSection(new Section("第一章", "内容", 1));
        Assert.assertFalse("缺少摘要的论文应校验失败", service.validate(invalidPaper));
    }

    @Test
    public void testValidatePaperWithNoSections() {
        Paper invalidPaper = new Paper("标题", "作者");
        invalidPaper.setAbstract("摘要内容");
        invalidPaper.addKeyword("关键词");
        Assert.assertFalse("缺少章节的论文应校验失败", service.validate(invalidPaper));
    }

    @Test
    public void testValidateNullPaper() {
        Assert.assertFalse("空论文对象应校验失败", service.validate(null));
    }

    @Test
    public void testGenerateTableOfContents() {
        String toc = service.generateTableOfContents(paper);
        Assert.assertNotNull("目录不应为null", toc);
        Assert.assertTrue("目录应包含'目录'标题", toc.contains("目录"));
        Assert.assertTrue("目录应包含章节名称", toc.contains("引言"));
        Assert.assertTrue("目录应包含结论章节", toc.contains("结论"));
        System.out.println("生成的目录：\n" + toc);
    }

    @Test
    public void testGenerateTableOfContentsWithSubSections() {
        paper.addSection(new Section("实验设置", "实验环境...", 2));
        String toc = service.generateTableOfContents(paper);
        String expectedIndent = "  "; // level-2 section has one level of indentation (2 spaces)
        Assert.assertTrue("目录应包含二级章节，且有缩进", toc.contains(expectedIndent + "实验设置"));
    }

    @Test
    public void testFormatReferences() {
        String refs = service.formatReferences(paper);
        Assert.assertNotNull("参考文献不应为null", refs);
        Assert.assertTrue("参考文献应包含编号[1]", refs.contains("[1]"));
        Assert.assertTrue("参考文献应包含编号[2]", refs.contains("[2]"));
        System.out.println("格式化的参考文献：\n" + refs);
    }

    @Test
    public void testFormatReferencesWithNoPaper() {
        String refs = service.formatReferences(null);
        Assert.assertEquals("空论文的参考文献应为空字符串", "", refs);
    }

    @Test
    public void testCountCharacters() {
        int charCount = service.countCharacters(paper);
        Assert.assertTrue("论文字符数应大于0", charCount > 0);
        // 标题(14) + 摘要(57) + 5个章节内容字符数之和
        int expectedMin = "基于深度学习的图像识别研究".length() + paper.getAbstractText().length();
        Assert.assertTrue("论文字符数应大于标题和摘要字符数之和", charCount >= expectedMin);
        System.out.println("论文字符数：" + charCount);
    }

    @Test
    public void testCountCharactersWithNullPaper() {
        int charCount = service.countCharacters(null);
        Assert.assertEquals("空论文字符数应为0", 0, charCount);
    }
}
