package club.sk1er.mods.sk1eroldanimations.asm;

import club.sk1er.mods.sk1eroldanimations.Sk1erOldAnimations;
import club.sk1er.mods.sk1eroldanimations.tweaker.transformer.ITransformer;
import net.minecraft.entity.player.EntityPlayer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class EntityPlayerTransformer implements ITransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.entity.player.EntityPlayer"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        FieldNode currentHeight = new FieldNode(Opcodes.ACC_PRIVATE, "currentHeight", "F", null, null);
        classNode.fields.add(currentHeight);

        FieldNode lastMillis = new FieldNode(Opcodes.ACC_PRIVATE, "lastMillis", "J", null, null);
        classNode.fields.add(lastMillis);

        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("<init>")) {
                methodNode.instructions.insertBefore(methodNode.instructions.getLast().getPrevious(), createHeightAndMillis());
            }

            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("getEyeHeight")) {
                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), changeEyeHeightInstructions());
//                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), changeEyeHeightHook());
            }
        }
    }

    private InsnList createHeightAndMillis() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new LdcInsnNode(1.62f));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayer", "lastMillis", "J"));
        return list;
    }

    private InsnList changeEyeHeightInstructions() {
        InsnList list = new InsnList();
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;", false));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "thePlayer", "Lnet/minecraft/client/entity/EntityPlayerSP;"));
        list.add(new VarInsnNode(Opcodes.ASTORE, 1));
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, Sk1erOldAnimations.getConfigClass(), "oldSneaking", "Z"));
        LabelNode ifeq = new LabelNode(); // L1
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq));
        list.add(new IntInsnNode(Opcodes.BIPUSH, 10));
        list.add(new VarInsnNode(Opcodes.ISTORE, 2));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayer", "isSneaking", "()Z", false));
        LabelNode ifeq2 = new LabelNode(); // L3
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq2));
        list.add(new LdcInsnNode(1.54f));
        list.add(new VarInsnNode(Opcodes.FSTORE, 3));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(new VarInsnNode(Opcodes.FLOAD, 3));
        list.add(new InsnNode(Opcodes.FCMPL));
        LabelNode ifle = new LabelNode(); // L6
        list.add(new JumpInsnNode(Opcodes.IFLE, ifle));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
        list.add(new VarInsnNode(Opcodes.LSTORE, 4));
        list.add(new VarInsnNode(Opcodes.LLOAD, 4));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "lastMillis", "J"));
        list.add(new InsnNode(Opcodes.LSUB));
        list.add(new VarInsnNode(Opcodes.LSTORE, 6));
        list.add(new VarInsnNode(Opcodes.LLOAD, 6));
        list.add(new VarInsnNode(Opcodes.ILOAD, 2));
        list.add(new InsnNode(Opcodes.I2L));
        list.add(new InsnNode(Opcodes.LCMP));
        list.add(new JumpInsnNode(Opcodes.IFLE, ifle)); // L6
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new InsnNode(Opcodes.DUP));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(new LdcInsnNode(0.012f));
        list.add(new InsnNode(Opcodes.FSUB));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new VarInsnNode(Opcodes.LLOAD, 4));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayer", "lastMillis", "J"));
        list.add(ifle);
        LabelNode gotoInsn = new LabelNode(); // L12
        list.add(new JumpInsnNode(Opcodes.GOTO, gotoInsn));
        list.add(ifeq2);
        list.add(new LdcInsnNode(1.62f));
        list.add(new VarInsnNode(Opcodes.FSTORE, 3));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(new VarInsnNode(Opcodes.FLOAD, 3));
        list.add(new InsnNode(Opcodes.FCMPG));
        LabelNode ifge = new LabelNode(); // L14
        list.add(new JumpInsnNode(Opcodes.IFGE, ifge));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(new LdcInsnNode(0.2f));
        list.add(new InsnNode(Opcodes.FCMPL));
        LabelNode ifle2 = new LabelNode(); // L14
        list.add(new JumpInsnNode(Opcodes.IFLE, ifle2));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
        list.add(new VarInsnNode(Opcodes.LSTORE, 4));
        list.add(new VarInsnNode(Opcodes.LLOAD, 4));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "lastMillis", "J"));
        list.add(new InsnNode(Opcodes.LSUB));
        list.add(new VarInsnNode(Opcodes.LSTORE, 6));
        list.add(new VarInsnNode(Opcodes.LLOAD, 6));
        list.add(new VarInsnNode(Opcodes.ILOAD, 2));
        list.add(new InsnNode(Opcodes.I2L));
        list.add(new InsnNode(Opcodes.LCMP));
        LabelNode ifle3 = new LabelNode(); // L18
        list.add(new JumpInsnNode(Opcodes.IFLE, ifle3));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new InsnNode(Opcodes.DUP));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(new LdcInsnNode(0.012f));
        list.add(new InsnNode(Opcodes.FADD));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new VarInsnNode(Opcodes.LLOAD, 4));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayer", "lastMillis", "J"));
        list.add(ifle3);
        list.add(new JumpInsnNode(Opcodes.GOTO, gotoInsn)); // L12
        list.add(ifge);
        list.add(ifle2);
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new LdcInsnNode(1.62f));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(gotoInsn);
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayer", "isPlayerSleeping", "()Z", false));
        LabelNode ifeq3 = new LabelNode(); // L21
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq3));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new LdcInsnNode(0.2f));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(ifeq3);
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "currentHeight", "F"));
        list.add(new InsnNode(Opcodes.FRETURN));
        list.add(ifeq);
        return list;
    }

    private InsnList changeEyeHeightHook() {
        InsnList list = new InsnList();
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, Sk1erOldAnimations.getConfigClass(), "oldSneaking", "Z"));
        LabelNode labelNode = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "club/sk1er/mods/sk1eroldanimations/production/EntityHook", "getEyeHeight", "()F", false));
        list.add(new InsnNode(Opcodes.FRETURN));
        list.add(labelNode);
        return list;
    }
}