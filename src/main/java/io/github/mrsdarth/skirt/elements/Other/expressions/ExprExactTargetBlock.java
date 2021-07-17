package io.github.mrsdarth.skirt.elements.Other.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.google.common.collect.Iterables;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


@Name("Exact target block")
@Description("returns the exact target block of a living entity, accounting for partial blocks such as stairs and doors")
@Examples("set {_b} to exact target block")
@Since("1.0.0")

public class ExprExactTargetBlock extends SimpleExpression<Block> {

    static {
        Skript.registerExpression(ExprExactTargetBlock.class, Block.class, ExpressionType.COMBINED,
                "[the] exact target[[t]ed] block[ of %livingentities%][ with max %-number% [metres]]");
    }

    private Expression<LivingEntity> entities;
    private Expression<Number> d;

    @Override
    public Class<? extends Block> getReturnType() {
        return Block.class;
    }

    @Override
    public boolean isSingle() {
        return entities.isSingle();
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        entities = (Expression<LivingEntity>) exprs[0];
        d = (Expression<Number>) exprs[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "exact target block";
    }

    @Override
    @Nullable
    protected Block[] get(Event event) {
        LivingEntity[] l = entities.getArray(event);
        Number d1 = (d != null) ? d.getSingle(event) : 100;
        if (d1 == null) return null;
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (LivingEntity p : l) {
            if (p != null) {
                blocks.add(p.getTargetBlockExact(d1.intValue()));
            }
        }
        return Iterables.toArray(blocks, Block.class);
    }
}