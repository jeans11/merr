(ns hooks.merr
  (:require
   [clj-kondo.hooks-api :as api]))

(defn merr-let
  [{:keys [:node]}]
  (let [[err-sym bindings & body] (rest (:children node))
        bindings (api/vector-node (concat [err-sym (api/token-node nil)]
                                          (:children bindings)))
        new-node (api/list-node
                  (list*
                   (api/token-node 'let)
                   bindings
                   body))]
    {:node new-node}))

(defn merr-match
  [{:keys [node]}]
  (let [[check-fn result-vec result-token error-vec error-token] (rest (:children node))
        [result-sym] (:children result-vec)
        [error-sym] (:children error-vec)
        sym-destructured (api/vector-node [result-sym error-sym])
        bindings (api/vector-node [sym-destructured check-fn])
        body (api/vector-node [result-token error-token])
        new-node (api/list-node
                   (list
                     (api/token-node 'let)
                     bindings
                     body))]
    {:node new-node}))
