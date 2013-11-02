/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataStructures;

import Utils.Functionals.TriFunction;
import java.awt.Color;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.StructureGraphic.v1.DSTreeNode;

/**
 *
 * @author Neo
 */
public class RedBlackTreeModule {
  public static abstract class Tree<K extends Comparable<K>, V> implements DSTreeNode {
    public abstract boolean isEmpty();
    public abstract boolean contains(final K key);
    public abstract Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value);
    public abstract Optional<V> get(final K key);
    public abstract V getWithDefault(final K key, final V def);
    public abstract Tree<K, V> remove(final K key);
    public abstract int size();
    public abstract int depth();
    public abstract void app(final Consumer<V> f);
    public abstract void appi(final BiConsumer<K, V> f);
    public abstract <W> Tree<K, W> map(final Function<V, W> f);
    public abstract <W> Tree<K, W> mapi(final BiFunction<K, V, W> f);
    public abstract <W> Tree<K, W> mapPartial(final Function<V, Optional<W>> f);
    public abstract <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f);
    public abstract <W> W foldl(final BiFunction<V, W, W> f, final W w);
    public abstract <W> W foldli(final TriFunction<K, V, W, W> f, final W w);
    public abstract <W> W foldr(final BiFunction<V, W, W> f, final W w);
    public abstract <W> W foldri(final TriFunction<K, V, W, W> f, final W w);
    public abstract Tree<K, V> filter(final Predicate<V> f);
    public abstract Tree<K, V> filteri(final BiPredicate<K, V> f);
    public abstract Tree<K, V> merge(final BiFunction<V, V, V> f, final Tree<K, V> t);
  }

  private static final class EmptyNode<K extends Comparable<K>, V> extends Tree<K, V> {
    private static final EmptyNode<? extends Comparable<?>, ?> sEmptyNode = new EmptyNode<>();

    @SuppressWarnings("unchecked")
    public static <K extends Comparable<K>, V> EmptyNode<K, V> createEmptyNode() {
      return (EmptyNode<K, V>) sEmptyNode;
    }

    @Override
    public boolean isEmpty() {
      return true;
    }

    @Override
    public boolean contains(final K key) {
      return false;
    }

    @Override
    public Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value) {
      return createEmptyNode();
    }

    @Override
    public Optional<V> get(final K key) {
      return Optional.empty();
    }

    @Override
    public V getWithDefault(final K key, final V def) {
      return def;
    }

    @Override
    public Tree<K, V> remove(final K key) {
      return this;
    }

    @Override
    public int size() {
      return 0;
    }

    @Override
    public int depth() {
      return 0;
    }

    @Override
    public void app(final Consumer<V> f) {
      return;
    }

    @Override
    public void appi(final BiConsumer<K, V> f) {
      return;
    }

    @Override
    public <W> Tree<K, W> map(final Function<V, W> f) {
      return createEmptyNode();
    }

    @Override
    public <W> Tree<K, W> mapi(BiFunction<K, V, W> f) {
      return createEmptyNode();
    }

    @Override
    public <W> Tree<K, W> mapPartial(final Function<V, Optional<W>> f) {
      return createEmptyNode();
    }

    @Override
    public <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f) {
      return createEmptyNode();
    }

    @Override
    public <W> W foldl(final BiFunction<V, W, W> f, final W w) {
      return w;
    }

    @Override
    public <W> W foldli(final TriFunction<K, V, W, W> f, final W w) {
      return w;
    }

    @Override
    public <W> W foldr(final BiFunction<V, W, W> f, final W w) {
      return w;
    }

    @Override
    public <W> W foldri(final TriFunction<K, V, W, W> f, final W w) {
      return w;
    }

    @Override
    public Tree<K, V> filter(final Predicate<V> f) {
      return this;
    }

    @Override
    public Tree<K, V> filteri(final BiPredicate<K, V> f) {
      return this;
    }

    @Override
    public Tree<K, V> merge(final BiFunction<V, V, V> f, final Tree<K, V> t) {
      return t;
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object DSgetValue() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color DSgetColor() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
  
  private static abstract class Node<K extends Comparable<K>, V> extends Tree<K, V> {
    private Node(final K key, final V value, final Tree<K, V> left, final Tree<K, V> right) {
      mKey = key;
      mValue = value;
      mLeft = left;
      mRight = right;
    }
 
    protected final K mKey;
    protected final V mValue;
    protected final Tree<K, V> mLeft;
    protected final Tree<K, V> mRight;
 
    public abstract <W> Node<K, W> createNode(final K key, final W newValue, final Tree<K, W> left, final Tree<K, W> right);

    @Override
    public boolean isEmpty() {
      return false;
    }

    @Override
    public boolean contains(final K key) {
      final int cmp = key.compareTo(mKey);
      if (cmp < 0) {
        return mLeft.contains(key);
      }
      else if (cmp > 0) {
        return mRight.contains(key);
      }
      else {
        return true;
      }
    }

    @Override
    public abstract Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value);

    @Override
    public Optional<V> get(final K key) {
      final int cmp = key.compareTo(mKey);
      if (cmp < 0) {
        return mLeft.get(key);
      }
      else if (cmp > 0) {
        return mRight.get(key);
      }
      else {
        return Optional.of(mValue);
      }
    }

    @Override
    public V getWithDefault(final K key, final V def) {
      return get(key).orElse(def);
    }

    @Override
    public abstract Tree<K, V> remove(final K key);

    @Override
    public int size() {
      return mLeft.size() + mRight.size();
    }

    @Override
    public int depth() {
      return Math.max(mLeft.depth(), mRight.depth()) + 1;
    }

    @Override
    public void app(final Consumer<V> f) {
      mLeft.app(f);
      f.accept(mValue);
      mRight.app(f);
      
      return;
    }

    @Override
    public void appi(final BiConsumer<K, V> f) {
      mLeft.appi(f);
      f.accept(mKey, mValue);
      mRight.appi(f);
      
      return;
    }

    @Override
    public <W> Tree<K, W> map(final Function<V, W> f) {
      return createNode(mKey, f.apply(mValue), mLeft.map(f), mRight.map(f));
    }
 
    @Override
    public <W> Tree<K, W> mapi(final BiFunction<K, V, W> f) {
      return createNode(mKey, f.apply(mKey, mValue), mLeft.mapi(f), mRight.mapi(f));
    }

    @Override
    public abstract <W> Tree<K, W> mapPartial(final Function<V, Optional<W>> f);

    @Override
    public abstract <W> Tree<K, W> mapPartiali(final BiFunction<K, V, Optional<W>> f);

    @Override
    public <W> W foldl(final BiFunction<V, W, W> f, final W w) {
      return mRight.foldl(f, f.apply(mValue, mLeft.foldl(f, w)));
    }

    @Override
    public <W> W foldli(final TriFunction<K, V, W, W> f, final W w) {
      return mRight.foldli(f, f.apply(mKey, mValue, mLeft.foldli(f, w)));
    }

    @Override
    public <W> W foldr(final BiFunction<V, W, W> f, final W w) {
      return mLeft.foldr(f, f.apply(mValue, mRight.foldr(f, w)));
    }

    @Override
    public <W> W foldri(final TriFunction<K, V, W, W> f, final W w) {
      return mLeft.foldri(f, f.apply(mKey, mValue, mRight.foldri(f, w)));
    }

    @Override
    public abstract Tree<K, V> filter(final Predicate<V> f);

    @Override
    public abstract Tree<K, V> filteri(final BiPredicate<K, V> f);

    @Override
    public Tree<K, V> merge(final BiFunction<V, V, V> f, final Tree<K, V> t) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public abstract DSTreeNode[] DSgetChildren();

    @Override
    public abstract Object DSgetValue();

    @Override
    public abstract Color DSgetColor();
  }

  private static final class RedNode<K extends Comparable<K>, V> extends Node<K, V> {
    private RedNode(final K key, final V value, final Tree<K, V> left, final Tree<K, V> right) {
      super(key, value, left, right);
    }

    @Override
    public <W> Node<K, W> createNode(final K key, final W newValue, final Tree<K, W> left, final Tree<K, W> right) {
      return new RedNode<>(key, newValue, left, right);
    }

    @Override
    public Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tree<K, V> remove(K key) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <W> Tree<K, W> mapPartial(Function<V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <W> Tree<K, W> mapPartiali(BiFunction<K, V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tree<K, V> filter(Predicate<V> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tree<K, V> filteri(BiPredicate<K, V> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object DSgetValue() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color DSgetColor() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

  private static final class BlackNode<K extends Comparable<K>, V> extends Node<K, V> {
    private BlackNode(final K key, final V value, final Tree<K, V> left, final Tree<K, V> right) {
      super(key, value, left, right);
    }

    @Override
    public <W> Node<K, W> createNode(final K key, final W newValue, final Tree<K, W> left, final Tree<K, W> right) {
      return new BlackNode<>(key, newValue, left, right);
    }

    @Override
    public Tree<K, V> insert(final BiFunction<V, V, V> f, final K key, final V value) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tree<K, V> remove(K key) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <W> Tree<K, W> mapPartial(Function<V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <W> Tree<K, W> mapPartiali(BiFunction<K, V, Optional<W>> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tree<K, V> filter(Predicate<V> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Tree<K, V> filteri(BiPredicate<K, V> f) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DSTreeNode[] DSgetChildren() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object DSgetValue() {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Color DSgetColor() {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
