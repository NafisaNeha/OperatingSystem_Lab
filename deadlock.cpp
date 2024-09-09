#include <bits/stdc++.h>

using namespace std;

bool DFS(char vertex, unordered_map<char, vector<char>>& adjList, unordered_map<char, int>& state, stack<char>& L) {
    state[vertex] = 1; // Mark the vertex as being in the current path
    L.push(vertex);

    for (const char neighbor : adjList[vertex]) {
        if (state[neighbor] == 0) {
            // Neighbor not visited
            if (DFS(neighbor, adjList, state, L)) {
                return true; // Cycle found
            }
        } else if (state[neighbor] == 1) {
            // Neighbor is in the current path, cycle found
            cout << "Deadlock detected involving the resource: " << neighbor << endl;
            // You can handle the deadlock here (e.g., release resources, terminate processes, etc.)
            return true;
        }
    }

    state[vertex] = 2; // Mark the vertex as visited and not in the current path
    L.pop(); // Remove the vertex from the current path
    return false;
}

void deadlockDetection(unordered_map<char, vector<char>>& adjList, stack<char>& L) {
    unordered_map<char, int> state;

    for (const auto& entry : adjList) {
        state[entry.first] = 0;
    }

    for (const auto& entry : adjList) {
        char vertex = entry.first;
        if (state[vertex] == 0) {
            if (DFS(vertex, adjList, state, L))
            {
                cout<<"Cycle found"<<endl;
                while (!L.empty())
                {
                    int element = L.top();
                    std::cout << (char)element << " ";
                    L.pop();
                }
                exit(0);
            }
        }
    }
}

int main() {
    unordered_map<char, vector<char>> adjList;
    stack<char> L;

    adjList['R'].push_back('A');
    adjList['A'].push_back('S');
    adjList['C'].push_back('S');
    adjList['W'].push_back('F');
    adjList['F'].push_back('S');
    adjList['D'].push_back('S');
    adjList['U'].push_back('D');
    adjList['D'].push_back('T');
    adjList['B'].push_back('T');
    adjList['T'].push_back('E');
    adjList['E'].push_back('V');
    adjList['V'].push_back('G');
    adjList['G'].push_back('U');
    adjList['S'] = vector<char>();

    deadlockDetection(adjList, L);

    return 0;
}
