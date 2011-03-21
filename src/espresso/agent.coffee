# agent (intra-process communication) api

global.espresso.agent = (name, callback) ->
  Packages.espresso.agent.Agents.registerAgent(name, this, callback)

global.espresso.Agent = class Agent
  constructor: (name) ->
    #todo validate that this agent exists
    @name = name

  send: (msg) ->
    Packages.espresso.agent.Agents.sendMessage(@name, msg)

  shutdown: ->
    Packages.espresso.agent.Agents.close(@name)