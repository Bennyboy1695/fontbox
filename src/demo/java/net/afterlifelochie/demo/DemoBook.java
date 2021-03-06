package net.afterlifelochie.demo;

import net.afterlifelochie.fontbox.api.DocumentBuilder;
import net.afterlifelochie.fontbox.api.IDocumentBuilder;
import net.afterlifelochie.fontbox.api.data.FormattedString;
import net.afterlifelochie.fontbox.api.data.IBookProperties;
import net.afterlifelochie.fontbox.api.data.IDocument;
import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.font.IGLFont;
import net.afterlifelochie.fontbox.api.formatting.PageMode;
import net.afterlifelochie.fontbox.api.formatting.PageProperties;
import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.formatting.layout.FloatMode;
import net.afterlifelochie.fontbox.api.formatting.layout.Layout;
import net.afterlifelochie.fontbox.api.formatting.style.ColorFormat;
import net.afterlifelochie.fontbox.api.formatting.style.DecorationStyle;
import net.afterlifelochie.fontbox.api.formatting.style.TextFormat;
import net.afterlifelochie.fontbox.render.GLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;

public class DemoBook implements IBookProperties {
    @DocumentBuilder
    public static IDocumentBuilder builder;

    private IDocument document;
    private IGLFont daniel, notethis, ampersand;

    public DemoBook() {
        try {
            /* Get some initial fonts */
            daniel = FontboxClient.getManager().fromName("Daniel");
            notethis = FontboxClient.getManager().fromName("Note this");
            ampersand = FontboxClient.getManager().fromName("Ampersand");

            /* Load the fable book */
            StringBuilder fable = new StringBuilder();
            IResource resource = Minecraft.getMinecraft().getResourceManager()
                .getResource(new ResourceLocation("fontbox", "books/fable.book"));
            InputStream stream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            char[] buf = new char[1024];
            int len = 0;
            while ((len = reader.read(buf)) != -1)
                fable.append(buf, 0, len);
            reader.close();

			/* Build the document */
            document = builder.createDocument();
            document.addImage(new ResourceLocation("fontbox", "textures/books/tortoise-rocket.png"), 128, 128, FloatMode.LEFT);
            document.addHeading("title", "The Tortoise and the Hare");
            document.addHeading("author", "Written by Aesop");

            document.floatBreak();
            document.addItemStack(new ItemStack(Items.DIAMOND, 1), 32, 32, AlignmentMode.CENTER);
            document.addParagraph(new FormattedString("The classic fable demonstration book thingy.")
                .applyFormat(new TextFormat(daniel, EnumSet.of(DecorationStyle.BOLD), new ColorFormat(128, 128, 255))));
            document.addParagraph(new FormattedString("The classic fable demonstration book thingy.")
                .applyFormat(new TextFormat(notethis, EnumSet.of(DecorationStyle.BOLD), new ColorFormat(128, 128, 255))));
            document.addParagraph(new FormattedString("The classic fable demonstration book thingy.")
                .applyFormat(new TextFormat(ampersand, EnumSet.of(DecorationStyle.BOLD), new ColorFormat(128, 128, 255))));
            document.pageBreak();

            String[] lines = fable.toString().split("\n");
            ArrayList<String> realLines = new ArrayList<>();
            for (String para : lines)
                if (para.trim().length() > 0)
                    realLines.add(para.trim());

            document.addItemStack(new ItemStack(Blocks.ANVIL, 1), 32, 32, FloatMode.LEFT);
            document.addParagraph(new FormattedString(realLines.get(0)));
            document.addItemStack(new ItemStack(Items.DIAMOND, 1), 32, 32, AlignmentMode.CENTER);
            document.addItemStack(new ItemStack(Items.APPLE, 1), 32, 32, FloatMode.LEFT);
            document.addParagraph(new FormattedString(realLines.get(1)).applyFormat(new TextFormat(daniel)));
            document.pageBreak();

            document.addHeading("ending", "The Finish");
            document.addItemStack(new ItemStack(Items.DIAMOND, 1), 32, 32, AlignmentMode.CENTER);
            document.addItemStack(new ItemStack(Items.GOLD_INGOT, 1), 32, 32, FloatMode.LEFT);
            document.addParagraph(new FormattedString(realLines.get(2)));

        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    public GuiScreen toGui() {
        try {
            return document.createBookGui(FontboxClient.getManager(), this);
        } catch (IOException | LayoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PageProperties getPageProperties() {
        /* Build some document properties */
        PageProperties properties = new PageProperties(400, 450, new TextFormat(daniel));
        properties.headingFormat(new TextFormat(notethis, EnumSet.of(DecorationStyle.BOLD, DecorationStyle.ITALIC), new ColorFormat(255, 128, 64)));
        properties.bodyFormat(new TextFormat(notethis));
        properties.bothMargin(2).lineHeightSize(30).spaceSize(4).densitiy(0.66f);
        return properties;
    }

    @Override
    public PageMode getPageMode() {
        return new PageMode(new Layout(15, 10), new Layout(195, 10));
    }

    @Override
    public void onPageChanged(GuiScreen gui, int whatPtr, int lastPointer) {
        /* No action required */
    }

    @Override
    public void drawBackground(int width, int height, int mx, int my, float frame, float zLevel) {
        // GLUtils isn't the api but I'm lazy
        GLUtils.useFontboxTexture("noteback");
        GLUtils.drawTexturedRectUV(0, 0, 400, 220, 0, 0, 1083.0f / 1111.0f, 847.0f / 1024.0f, zLevel);
    }

    @Override
    public void drawForeground(int width, int height, int mx, int my, float frame, float zLevel) {
    }

    @Override
    public int getBookHeight() {
        return 220;
    }

    @Override
    public int getBookWidth() {
        return 400;
    }
}
