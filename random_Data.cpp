#include <iostream>
#include <stdlib.h>
#include <fstream>
#include <random>
using namespace std;

int main()
{
    random_device rd; // random seed
    mt19937 mt(rd()); // random number function with seed
    // uniform dist for generating
    // 1, 50
    uniform_int_distribution<int> no_of_items(1, 50); // upper and lower bounds
    // 100, 5000
    uniform_int_distribution<int> price(100, 5000); // upper and lower bounds

    const int MILLION = 1000000;
    ofstream file("random_Data.csv");

    for (int i = 0; i < 10 * MILLION; i++)
    {
        int items = no_of_items(mt);
        bool last = false;
        for (int j = 0; j < items; j++)
        {
            int prices = price(mt);
            file << prices;
            if (j == items - 1)
            {
                last = true;
            }
            if (!last)
            {
                file << ",";
            }
        }
        file << "\n";
    }
}
