# basic platform include.

# global context (necessary for registering stuff in the global scope)
global = this
global.espresso = {}

console =
  log: (text) ->
    java.lang.System.out.println('' + text)

require = (module) ->
  # don't include what we have already included
  if require[module]?
    return
  require[module] = true
  js = espresso.fetchLib(module)

  # wrap js in function to force evaluation
  eval( "(function(global) { #{js} })(this);" )
