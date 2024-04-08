(ns repro.fail
  (:import io.netty.handler.codec.compression.BrotliOptions))

(defn bar []
  BrotliOptions)

(defn -main [& args]
  (prn (bar)))
