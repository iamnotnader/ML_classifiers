#include <stdio.h>
#include <stdlib.h>

int main(int argc, char* argv[])
{
	FILE* fin = fopen(argv[1], "r");
	FILE* fount = fopen("temp.out", "rw");
	int x;
	fscanf(fin,"%d", &x);
	printf("%d\n", x);
	return 0;
}


