# Reproducer for `fn` compilation issue

This project is a reproducer for the following issue: Given a Clojure function which references a
class, if that class has a static field with an initializer which fails when run, the function will
fail to compile because the compiler (needlessly) runs the initializer even if the respective field
is not accessed.

See `repro.fail` namespace for an example. Here, the `bar` function references the
`io.netty.handler.codec.compression.BrotliOptions` class. That class has [a package-private static
field named
`DEFAULT`](https://github.com/netty/netty/blob/c1d0fd2dbf7907a99ab702795e35d9de8068c1cb/codec/src/main/java/io/netty/handler/codec/compression/BrotliOptions.java#L32-L34)
with an initializer. That initializer depends on a class which is provided by an optional
third-party library. However, even though `bar` never references that field, the initializer gets
run and fails because that library is not present.

Run it like this:

```
$ clojure -M -m repro.fail
Execution error (ClassNotFoundException) at jdk.internal.loader.BuiltinClassLoader/loadClass (BuiltinClassLoader.java:641).
com.aayushatharva.brotli4j.encoder.Encoder$Parameters
```

## Workaround

The issue can be worked around by putting the class value into a separate `def` and using that in
the function instead. See `repro.ok`. Run it like this:

```
$ clojure -M -m repro.ok
io.netty.handler.codec.compression.BrotliOptions
```

## Relevance

The issue was encountered in the context of https://github.com/clj-commons/aleph/issues/703
