/****************************************************************
  Cryptarithmetic Puzzle Solver
  by Naoyuki Tamura (tamura@kobe-u.ac.jp)
****************************************************************/  
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <setjmp.h>
#include <time.h>
#include <string.h>
#include <limits.h>

void quit();
int parse();
void command();
int crypt();
char *setup();
int min_value();
int max_value();
int mini();
int maxi();
void solve();
void show_puzzle();
void show_sol();
long usertime();

#define	BASE		(10)
#define	PUZ_SIZE	(1000)
#define	YES		(1)
#define	NO		(0)
#define	quot(x,y)	((x)/(y))
#define	mod(x,y)	((x)%(y))
#define putstr(s)	fputs(s, stdout)

char **puzzle;		/* e.g. {"SEND", "MORE", "MONEY", NULL} */
char puz[PUZ_SIZE];
// * keep track of (selected)letter and corresponding value
int sel[UCHAR_MAX];
int sel_copy[UCHAR_MAX];

// * keep track of the lowest possible value of each char. If letter is first letter of string, its value >= 1.
int lower[UCHAR_MAX];
// * keep track of used digit
int used[BASE];
int count;
int limit = 0;
int unique = NO;
int num_tested = 0;
int num_unique = 0;
jmp_buf jmpbuf;
long usertime0;

char *example[] = {
    "A+MERRY+XMAS=TURKEY",
    "ADAM+AND+EVE=MOVED",
    "ALPHABET+LETTERS=SCRABBLE",
    "APPLE+GRAPE+PLUM=BANANA",
    "BASE+BALL=GAMES",
    "BEST+MAKE=MASER",
    "BILL+WILLIAM+MONICA=CLINTON",
    "BONGO+BONGO+BONGO+ON+THE=CONGO",
    "COCA+COLA=OASIS",
    "CROSS+ROADS=DANGER",
    "CRYPT+ARITH+METIC=CREATE",
    "DID+HE+READ+HER=DIARY",
    "DONALD+GERALD=ROBERT",
    "DOS+DOS+TRES=SIETE",
    "EARTH+AIR+WATER+FIRE=NATURE",
    "EIN+EIN+EIN+EIN=VIER",
    "FIFTY+STATES=AMERICA",
    "GEE+I+SEE+A+RARE+MAGIC=SQUARE",
    "GRAPE+APPLE=CHERRY",
    "LEARN+LINEAR+LOGIC=PROLOG",
    "LISP+LISP+LOGIC+LOGIC=PROLOG",
    "LLP+LINEAR+LOGIC=PROLOG",
    "LOGIC+JAVA+JAVA=PROLOG",
    "LOGIC+LOGIC=PROLOG",
    "LYNNE+LOOKS=SLEEPY",
    "MONEY+POP+SOME+MONEY=PLEASE",
    "NEPTUNE+SATURN+URANUS+PLUTO=PLANETS",
    "NEVER+ALWAYS=NEARLY",
    "NO+SNOW+IN+VIEW+ON+ROOFS+IN=VENICE",
    "OSAKA+HAIKU+SUSHI=JAPAN",
    "PEANUT+TEETH=CARTER",
    "PRIME+CRYPT+ARITH=METIC",
    "RIDE+DRIVE=NEVER",
    "SEND+MORE=MONEY",
    "TERRIBLE+NUMBER=THIRTEEN",
    "THIS+ISA+GREAT+TIME=WASTER",
    "USA+USSR=PEACE",
    "WE+WANT+NO+NEW+ATOMIC=WEAPON",
    "WHO+IS+THIS=IDIOT",
    "WINTER+IS+WINDIER+SUMMER+IS=SUNNIER",
    "SIX+SEVEN+SEVEN=TWENTY",
    "TEN+TEN+FORTY=SIXTY",
    "ONE+NINE+TWENTY+FIFTY=EIGHTY",
    "THREE+SEVEN+TEN+TWENTY+THIRTY=SEVENTY",
    "FIVE+SEVEN+ELEVEN+TWELVE+FIFTEEN+TWENTY=SEVENTY",
    "SIX+SEVEN+NINE+TWELVE+SIXTEEN+TWENTY=SEVENTY",
    "ONE+TWO+FIVE+NINE+ELEVEN+TWELVE+FIFTY=NINETY",
    "UN+UN+NEUF=ONZE",
    "EINS+ZWEI+SIEBEN+SECHZIG=SIEBZIG",
    "SEIS+CATORCE+SETENTA=NOVENTA",
    "WWW+BUG+BILL=GATES",
    "WWW+WS+UNIX=CRASH",
    "OSAKA+KYOTO=TOKYO",
    "TOMATO+POTATO=PUMPKIN",
    "FUJITSU+FUJITSU=HITACHI",
    "SYSTEM5+ERROR=SOLARIS"
};

/*
char *version = "Cryptarithmetic Puzzle Solver 1.1 (Nov. 11, 1998)";
char *version = "Cryptarithmetic Puzzle Solver 1.2 (Dec. 4, 1998)";
char *version = "Cryptarithmetic Puzzle Solver 1.3 (Jan. 6, 1999)";
*/
char *version = "Cryptarithmetic Puzzle Solver 1.4 (Oct. 21, 1999)";
char *copyright = "Copyright (C) Naoyuki Tamura (tamura@kobe-u.ac.jp)";
char *organization = "Department of CS, Kobe University, Japan";
char *www = "http://bach.seg.kobe-u.ac.jp/tamura.html";