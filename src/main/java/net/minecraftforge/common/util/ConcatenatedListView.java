package net.minecraftforge.common.util;

import java.util.AbstractList;
import java.util.List;

public class ConcatenatedListView
{
    @SafeVarargs
    public static <T> List<T> of(List<T>... lists)
    {
        return new AbstractList<>()
        {
            @Override
            public T get(int index)
            {
                for (List<T> list : lists)
                {
                    if (index < list.size())
                    {
                        return list.get(index);
                    }
                    index -= list.size();
                }
                throw new IndexOutOfBoundsException("Index: " + index);
            }

            @Override
            public int size()
            {
                int size = 0;
                for (List<T> list : lists)
                {
                    size += list.size();
                }
                return size;
            }
        };
    }
}
