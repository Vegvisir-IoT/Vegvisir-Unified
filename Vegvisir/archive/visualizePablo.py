
import networkx as nx
import matplotlib.pyplot as plt
import plotly
import plotly.plotly as py
from plotly.graph_objs import *
from collections import deque

from vegvisir import *



def walk(block, graph, chain):
    """ Depth-first traversal of blockchain to create graph"""
    # graph.add_node(block.hash())

    q = deque()
    for block_hash in chain.frontier_nodes:
        graph.add_node(block_hash)
        q.append(chain.blocks[block_hash]) # init a deque with the frontier
        
    while len(q) > 0:
        b = q.pop()
        if not isinstance(b, GenesisBlock):
            for parent_hash in b.parents:
                parent = chain.blocks[parent_hash]
                graph.add_node(parent_hash)
                graph.add_edge(parent_hash, b.hash())
                q.appendleft(parent)



    # q = deque()
    # q.append(block)
    # while len(q) > 0:
    #     b = q.pop()
    #     for child_hash in b.children:
    #         child = chain.blocks[child_hash]
    #         graph.add_node(child_hash)
    #         graph.add_edge(child_hash, b.hash())
    #         q.appendleft(child)


def visualize(blockchain):
    visualize_matplotlib(blockchain)


def visualize_matplotlib(blockchain):
    graph = nx.DiGraph()
    # Walk through the chain and add nodes and edges
    walk(blockchain.genesis_block, graph, blockchain)
    plt.subplot(111)
    nx.draw(graph)
    plt.show()

def get_edge_trace(graph):
    edge_trace = Scatter(
        x=[],
        y=[],
        line=scatter.Line(width=0.5, color='#888'),
        hoverinfo='none',
        mode='lines')
    for edge in graph.edges():
        x0, y0 = graph.node[edge[0]]['pos']
        x1, y1 = graph.node[edge[1]]['pos']
        edge_trace['x'] += (x0, x1, None)
        edge_trace['y'] += (y0, y1, None)
    return edge_trace

def get_node_trace(graph):
    node_trace = Scatter(
        x=[],
        y=[],
        text=[],
        mode='markers',
        hoverinfo='text',
        marker=dict(
            size=30,
            color='rgba(152, 0, 0, 0.8)',
            line = dict(width=2, color='rgb(152,0,0)')
            )
    )
    i = 0
    for node in graph.nodes():
        x, y = graph.node[node]['pos']
        node_trace['x'] += (i,)
        node_trace['y'] += (y*10,)
        graph.node[node]['pos'] = (i, y*10)
        i = i + 50
    return node_trace

def set_layout(graph):
    pos = nx.layout.spring_layout(graph)
    for node in pos:
        graph.node[node]['pos'] = pos[node]

def add_annotations(graph, node_trace, blockchain):
    for node in graph.nodes():
        block = blockchain.blocks[node]
        string = 'Userid: {0}, Timestamp: {1}'.format(block.userid, block.timestamp)
        if isinstance(block, GenesisBlock):
            string += ', Genesis block'
        else:
            #string += ', Normal block'
            tx = ', recordid: {0}, comment: {1}'.format(block.tx[0].recordid,
                                                        block.tx[0].comment.decode('utf-8'))
            string += tx
        node_trace['text'] += (string,)

def visualize_plotly(blockchain):
    graph = nx.DiGraph()
    walk(blockchain.genesis_block, graph, blockchain)
    set_layout(graph)
    node_trace = get_node_trace(graph)
    edge_trace = get_edge_trace(graph)
    add_annotations(graph, node_trace, blockchain) 
    fig = Figure(data=Data([edge_trace, node_trace]),
                 layout=Layout(
                     title='<br>Blockchain graph made with plotly',
                     titlefont=dict(size=16),
                     showlegend=False,
                     hovermode='closest',
                     margin=dict(b=20, l=5, r=5, t=40),
                     xaxis=layout.XAxis(showgrid=False, zeroline=False, showticklabels=False),
                     yaxis=layout.YAxis(showgrid=False, zeroline=False, showticklabels=False)))
    plotly.offline.plot(fig, filename='networkx.html')



