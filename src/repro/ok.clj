(ns repro.ok
  (:import io.netty.handler.codec.compression.BrotliOptions))

(def foo BrotliOptions)

(defn bar []
  foo)

(defn -main [& args]
  (prn (bar)))
