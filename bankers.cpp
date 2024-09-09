#include<bits/stdc++.h>

using namespace std;


int main()
{
    int noProcess,noResource;
    cout<<"Enter the no. of processes: ";
    cin>>noProcess;
    cout<<"Enter the no. of resources: ";
    cin>>noResource;
    int maximum[noProcess][noResource];
    int allocated[noProcess][noResource];
    int need[noProcess][noResource];
    int available[noResource];
    int total[noResource];
    int work[noResource];
    bool finish[noProcess];

    vector<int> sequence;

    for(int i=0;i<noProcess;i++)
    {
        cout<<endl;
        cout<<"Process "<<i+1;
        for(int j=0;j<noResource;j++)
        {
            cout<<"Maximum value for resource "<<j+1<<":";
            cin>>maximum[i][j];

        }
        for(int j=0;j<noResource;j++)
        {
            cout<<"Allocated value for resource "<<j+1<<":";
            cin>>allocated[i][j];
            need[i][j] = maximum[i][j] - allocated[i][j];
        }
        finish[i] = false;
        cout<<endl;
    }
    cout<<endl;
    for(int i=0;i<noResource;i++)
    {
        cout<<"Enter total value of resource "<<i+1<<":";
        cin>>total[i];
    }
    cout<<endl;
    for(int i=0;i<noResource;i++)
    {
        int temp = 0;
        for(int j=0;j<noProcess;j++)
        {
            temp += allocated[j][i];
        }
        available[i] =total[i] - temp;
        work[i] = available[i];
    }

    for(int step=0;step<noProcess;step++)
    {
        bool found = false;
        for(int i=0;i<noProcess;i++)
        {
            if(!finish[i])
            {
                bool isSafe = true;
                for (int j = 0; j < noResource; j++)
                {
                    if (need[i][j] > work[j]) {
                        isSafe = false;
                        break;
                    }
                }
                if(isSafe)
                {
                    for (int j = 0; j < noResource; j++) {
                        work[j] += allocated[i][j];
                    }
                    finish[i] = true;
                    sequence.push_back(i+1);
                    found = true;
                    break;
                }
            }
        }
        if(!found)
        {
            cout<<"The system is in unsafe state"<<endl;
            exit(0);

        }
    }

    cout << "The system is in a safe state. Safe sequence: ";
    for (int i = 0; i < sequence.size(); ++i)
    {
        cout << "P" << sequence[i];
        if (i < sequence.size() - 1) {
            cout << " -> ";
        }
    }
    cout << endl;
}
