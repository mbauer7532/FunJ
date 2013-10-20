/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Functionals.TriFunction;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author Neo
 */
public final class IntMapModule {
  public static abstract class Tree<V> {
    public abstract boolean isEmpty();
    public abstract boolean contains(final int key);
    public abstract void insert(final int key, final V value);
    public abstract V get(final int key);
    public abstract V getWithDefault(final int key, final V def);
    public abstract Tree<V> remove(final int key);
    public abstract int size();
    public abstract void app(final Consumer<V> f);
    public abstract void appi(final BiConsumer<Integer, V> f);
    public abstract <W> Tree<W> map(final Function<V, W> f);
    public abstract <W> Tree<W> mapi(final BiFunction<Integer, V, W> f);
    public abstract <W> Tree<W> mapPartial(final Function<V, Optional<W>> f);
    public abstract <W> Tree<W> mapPartiali(final BiFunction<Integer, V, Optional<W>> f);
    public abstract <W> W foldl(final BiFunction<V, W, W> f, final W w);
    public abstract <W> W foldli(final TriFunction<Integer, V, W, W> f, final W w);
    public abstract <W> W foldr(final BiFunction<V, W, W> f, final W w);
    public abstract <W> W foldri(final TriFunction<Integer, V, W, W> f, final W w);
    public abstract Tree<V> filter(final Predicate<V> f);
    public abstract Tree<V> filteri(final BiPredicate<Integer, V> f);
    public abstract Tree<V> merge(final BiFunction<V, V, V> f, final Tree<V> t);
  }

  private static final class EmptyNode<V> extends Tree<V> {

    @Override
    public void insert(int key, V value) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V get(int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V getWithDefault(int key, V def) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> remove(int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void app(Consumer<V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appi(BiConsumer<Integer, V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> map(Function<V, W> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> mapi(BiFunction<Integer, V, W> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> mapPartial(Function<V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> mapPartiali(BiFunction<Integer, V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldl(BiFunction<V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldli(TriFunction<Integer, V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldr(BiFunction<V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldri(TriFunction<Integer, V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> filter(Predicate<V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> filteri(BiPredicate<Integer, V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> merge(BiFunction<V, V, V> f, Tree<V> t) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  }

  private static final class LeafNode<V> extends Tree<V> {
    private LeafNode(final int j, final V val) {
      mj = j;
      mVal = val;
    }

    private final int mj;
    private final V mVal;

    @Override
    public void insert(int key, V value) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V get(int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V getWithDefault(int key, V def) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> remove(int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void app(Consumer<V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appi(BiConsumer<Integer, V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> map(Function<V, W> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> mapi(BiFunction<Integer, V, W> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> mapPartial(Function<V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> mapPartiali(BiFunction<Integer, V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldl(BiFunction<V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldli(TriFunction<Integer, V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldr(BiFunction<V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldri(TriFunction<Integer, V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> filter(Predicate<V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> filteri(BiPredicate<Integer, V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> merge(BiFunction<V, V, V> f, Tree<V> t) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  }

  private static final class BranchNode<V> extends Tree<V> {
    private BranchNode(
            final int p,
            final int m,
            final Tree<V> left,
            final Tree<V> right) {
      mp = p;
      mm = m;
      mLeft = left;
      mRight = right;
    }

    private final int mp;
    private final int mm;
    private final Tree<V> mLeft;
    private final Tree<V> mRight;

    @Override
    public void insert(int key, V value) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean contains(int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V get(int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V getWithDefault(int key, V def) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> remove(int key) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int size() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void app(Consumer<V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void appi(BiConsumer<Integer, V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> map(Function<V, W> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> mapi(BiFunction<Integer, V, W> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> mapPartial(Function<V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> Tree<W> mapPartiali(BiFunction<Integer, V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldl(BiFunction<V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldli(TriFunction<Integer, V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldr(BiFunction<V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <W> W foldri(TriFunction<Integer, V, W, W> f, W w) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> filter(Predicate<V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> filteri(BiPredicate<Integer, V> f) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Tree<V> merge(BiFunction<V, V, V> f, Tree<V> t) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  }

  private static final EmptyNode<?> sEmptyNode = new EmptyNode<>();

  @SuppressWarnings("unchecked")
  public static <V> Tree<V> empty() {
    return (Tree<V>) sEmptyNode;
  }

  public static <V> Tree<V> singleton() {
    return null;
    /*
    final Tree<V> e = empty();
    return new LeafNode();
    */
  }
}
