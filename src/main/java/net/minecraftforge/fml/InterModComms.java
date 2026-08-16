package net.minecraftforge.fml;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class InterModComms
{
    public static class IMCMessage
    {
        private final String sender;
        private final String method;
        private final Supplier<?> messageSupplier;

        public IMCMessage(String sender, String method, Supplier<?> messageSupplier)
        {
            this.sender = sender;
            this.method = method;
            this.messageSupplier = messageSupplier;
        }

        public String sender() { return sender; }
        public String method() { return method; }
        public Supplier<?> messageSupplier() { return messageSupplier; }
    }

    public static boolean sendTo(String modId, String method, Supplier<?> message)
    {
        return false;
    }

    public static boolean sendTo(String modId, String method, Supplier<?> message, Runnable callback)
    {
        return false;
    }
}
