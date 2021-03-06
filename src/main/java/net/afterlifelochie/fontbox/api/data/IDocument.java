package net.afterlifelochie.fontbox.api.data;

import net.afterlifelochie.fontbox.api.FontboxManager;
import net.afterlifelochie.fontbox.api.exception.LayoutException;
import net.afterlifelochie.fontbox.api.formatting.layout.AlignmentMode;
import net.afterlifelochie.fontbox.api.formatting.layout.CompilerHint;
import net.afterlifelochie.fontbox.api.formatting.layout.FloatMode;
import net.afterlifelochie.fontbox.api.layout.IElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Collection;

public interface IDocument {
    void addElement(IElement element);

    default void addElements(Collection<IElement> elements) {
        elements.forEach(e -> addElement(e));
    }

    default void addHeading(String uid, String text) {
        addHeading(uid, new FormattedString(text));
    }

    void addHeading(String uid, FormattedString text);

    default void addParagraph(String text) {
        addParagraph(new FormattedString(text));
    }

    void addParagraph(FormattedString text);

    void addParagraph(FormattedString text, AlignmentMode align);

    default void addLink(String text, String toUid) {
        addLink(new FormattedString(text), toUid);
    }

    void addLink(FormattedString text, String toUid);

    void addImage(ResourceLocation location, int width, int height);

    void addImage(ResourceLocation location, int width, int height, AlignmentMode align);

    void addImage(ResourceLocation location, int width, int height, FloatMode floating);

    void addImage(ResourceLocation location, int width, int height, AlignmentMode align, FloatMode floating);

    void addItemStack(ItemStack itemStack, int width, int height);

    void addItemStack(ItemStack itemStack, int width, int height, AlignmentMode align);

    void addItemStack(ItemStack itemStack, int width, int height, FloatMode floating);

    void addItemStack(ItemStack itemStack, int width, int height, AlignmentMode align, FloatMode floating);

    void addCompilerHint(CompilerHint hint);

    default void pageBreak() {
        addCompilerHint(CompilerHint.PAGE_BREAK);
    }

    default void floatBreak() {
        addCompilerHint(CompilerHint.FLOAT_BREAK);
    }

    GuiScreen createBookGui(FontboxManager manager, IBookProperties bookProperties) throws IOException, LayoutException;

    IBook createBook(FontboxManager manager, IBookProperties bookProperties) throws IOException, LayoutException;
}
