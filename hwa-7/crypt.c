/****************************************************************
  Cryptarithmetic Puzzle Solver
  by Naoyuki Tamura (tamura@kobe-u.ac.jp)
****************************************************************/
#include "crypt.h"

int main(int argc, char *argv[]) {
  char buff[PUZ_SIZE];
  char *pp[PUZ_SIZE];
  int len, s, i;

  usertime0 = usertime();
  srand((unsigned int)time(NULL));
  putstr("Cryptarithmetic Puzzle Solver by N. Tamura ");
  putstr("(type help for help)\n");
  fflush(stdout);
  while (fgets(buff, PUZ_SIZE, stdin) != NULL) {
    if (*buff == '\n' || *buff == '#')
      continue;
    len = parse(buff, pp);
    if (len == 0) {
      putstr("Parse error\n");
      continue;
    }
    if (strcmp(pp[0], "example") == 0 && pp[1] == NULL) {
      s = (sizeof example / sizeof(char *));
      i = rand() % s;
      printf("Example %d/%d\n", i, s);
      strcpy(buff, example[i]);
      parse(buff, pp);
      crypt(pp);
    } else if (len <= 2) {
      command(pp[0], pp[1]);
    } else {
      char *strs[4] = {"CBEHEIDGEI", "CBEHEIDGEI", "BBBBBBBBBB", NULL};
      // pp = &strs;
      crypt(strs);
    }
    fflush(stdout);
  }
  quit();
}

void quit() {
  if (unique) {
    printf("%d unique-solution puzzles found ", num_unique);
    printf("(%d puzzles examined)\n", num_tested);
  }
  printf("Total time = %ld msec.\n", usertime() - usertime0);
  exit(0);
}
/**
 * Parses the input string
 */
int parse(char *q, char **pp) {
  int len = 0;
  while (*q) {
    if (isspace(*q) || *q == '+' || *q == '=') {
      *q++ = '\0';
      continue;
    }
    if (isalnum(*q)) {
      *pp++ = q;
      ++len;
      while (isalnum(*q))
        ++q;
      continue;
    }
    return 0;
  }
  *pp = NULL;
  return len;
}

/**
 * Command line interface control
 */
void command(char *cmd, char *arg) {
  if (strcmp(cmd, "help") == 0) {
    putstr("  SEND MORE MONEY : this is an example\n");
    putstr("  help            : show this message\n");
    putstr("  example         : show an example at random\n");
    putstr("  limit n         : show at most n solutions ");
    putstr("(0 means no-limit)\n");
    putstr("  quit            : quit\n");
  } else if (strcmp(cmd, "limit") == 0 && arg != NULL) {
    limit = atoi(arg);
  } else if (strcmp(cmd, "unique") == 0 && arg != NULL) {
    unique = atoi(arg);
  } else if (strcmp(cmd, "version") == 0) {
    putstr(version);
    putstr("\n");
    putstr(copyright);
    putstr("\n");
    putstr(organization);
    putstr("\n");
    putstr(www);
    putstr("\n");
  } else if (strcmp(cmd, "quit") == 0) {
    quit();
  } else {
    putstr("Unknown command\n");
  }
}

/**
 * Actual function to find cryptarithmetic.
 * @param pp char array of addends and sum.
 */
int crypt(char **pp) {
  char *msg;
  int d;
  long t0 = usertime();

  ++num_tested;
  puzzle = pp;
  count = 0;
  // initalized used array
  for (d = 0; d < BASE; ++d) {
    used[d] = NO;
  }

  if (unique) {
    /* search only unique solution puzzle */
    if (setup() != NULL)
      return NO;
    limit = 2;
    if (setjmp(jmpbuf) == 0) {
      solve(puz, 0);
      /* return after finding all solutions */
      if (count == 1) {
        /* unique solution */
        ++num_unique;
        memcpy(sel, sel_copy, sizeof(sel));
        show_puzzle();
        putstr(" (");
        show_sol();
        putstr(")\n");
      }
    }
  } else {
    /* usual search */
    putstr("  ");
    show_puzzle();
    putstr("\n");
    if ((msg = setup()) != NULL) {
      printf("  Wrong input (%s)\n", msg);
      return NO;
    }
    if (setjmp(jmpbuf) == 0) {
      solve(puz, 0);
      /* return after finding all solutions */
      printf("  %d solution(s)", count);
    } else {
      /* return by longjmp */
      printf("  %d or more solution(s)", count);
    }
    printf(", %ld msec.\n", usertime() - t0);
  }
  return YES;
  
}
/**
 * Perform inital setup, i.e copying the inputs to puz[]
 * and keeping track of first Letter of each word.
 */
char *setup() {
  int c, maxlen, min, max, k, i, j;
  char **pp, *p;

  /* initialize sel[] */
  for (c = 0; c < UCHAR_MAX; ++c) {
    sel[c] = -2;
  }
  for (c = '0'; c <= '9'; ++c) {
    sel[c] = c - '0';
  }
  /* check puzzle */
  pp = puzzle;
  if (*puzzle == NULL || *(puzzle + 1) == NULL)
    return "too short";
  maxlen = 0;
  min = 0;
  max = 0;

  // * for each addend in array ( but not sum string)
  for (pp = puzzle; *(pp + 1) != NULL; ++pp) {
    if (strlen(*pp) == 0) {
      return NO;
    }

    // * get the longest string length
    maxlen = maxi(maxlen, strlen(*pp));

    // * find the max and min value possible for this string.
    min += min_value(*pp); // * find the min value of the sum, since min_sum ==
                           // min_add1 + min_add2
    max += max_value(*pp); // * find the max value of the sum since max_sum ==
                           // max_add1 + max_add2
  }

  // * get the biggest string length (could be one of addned or the sums length)
  maxlen = maxi(maxlen, strlen(*pp));

  if (maxi(min, min_value(*pp)) > mini(max, max_value(*pp))) {
    // return "range error";
  }

  k = 0; // * keep track of how many words found.
  // * for each string in puzzle array
  for (pp = puzzle; *pp != NULL; ++pp) {
    // * for each char in string
    for (p = *pp; *p; ++p) {
      // * if its an alphabet char, and char has default value
      if (isalpha(*p) && sel[(unsigned)*p] == -2) {
        sel[(unsigned)*p] = -1;
        ++k;
      }
    }
  }
  if (k > BASE)
    return "too many alphabets";
  /**
   *  SEND
   *  MORE
   * MONEY
   */
  /* initialize puz[]
 e.g. puz = "+D+E=Y+N+R=E+E+O=N+S+M=O=M"; */
  p = puz;

  /**
   * puz stores the chars from addend and sum from LSD (least siginificant
   * digit) to MSB
   */
  for (i = 1; i <= maxlen; ++i) {
    for (pp = puzzle; *pp != NULL; ++pp) {
      // * get position
      j = strlen(*pp) - i;

      if (j < 0)
        continue;
      // * determine if next string is addend or sum
      if (*(pp + 1) != NULL)
        *p++ = '+'; // * next string is addend
      else
        *p++ = '='; // * next string is sum
      *p++ = *(*pp + j);
      if (p >= puz + PUZ_SIZE)
        return "too long input";
    }
  }
  *p = '\0';
  /* initialize lower[]
 e.g. lower['S'] = 1; lower['M'] = 1; */
  for (c = 0; c < UCHAR_MAX; ++c) {
    lower[c] = 0;
  }
  // * mark the first letter of each word.
  for (pp = puzzle; *pp != NULL; ++pp)
    if (isalpha(**pp))
      lower[**pp] = 1;
  return NULL;
}

/**
 * Finds the minimum value of a given string based on its length.
 * For example, string HELLO can have max value 10000 (but no less)
 *
 * @param p string to calculate minimum value of
 * @return mimimum numerical value the string can possibly have.
 */
int min_value(char *p) {
  int min = 0;
  char *q;
  // iterate to the end of string, at End of String, *q == NULL == 0/False.
  for (q = p; *q; ++q) {
    min *= BASE;
    if (isalpha(*q))
      min += (q == p) ? 1 : 0;
    else if (isdigit(*q))
      min += *q - '0';
  }
  return min;
}

/**
 * Finding the max possible value a string can have
 * For example, string HELLO can have max value 99999 (but no more)
 * @param p string to calculate value of
 * @return max possible length
 */
int max_value(char *p) {
  int max = 0;
  for (; *p; ++p) {
    max *= BASE;
    if (isalpha(*p))
      max += BASE - 1;
    else if (isdigit(*p))
      max += *p - '0';
  }
  return max;
}

/**
 * return minimum value between x and y
 * @param x
 * @param y
 * @return min value
 */

int mini(int x, int y) { return (x < y) ? x : y; }

/**
 * return maximum value between x and y
 * @param x
 * @param y
 * @return max value
 */
int maxi(int x, int y) { return (x > y) ? x : y; }

/**
 * Caluclate the sum and store it in global vars.
 * @param p string to calculate sum of/
 * @param s total sum of the string so far.
 */
void solve(char *p, int s) {
  register unsigned int c; // first char of the string.
  register int d;          // value of c

  // * when we have found the sum of string
  if (*p == '\0' && s == 0) {
    /* solved */
    ++count;
    if (unique) {
      memcpy(sel_copy, sel, sizeof(sel));
    } else {
      putstr("  ");
      show_sol();
      putstr("\n");
    }
    if (limit > 0 && count >= limit)
      longjmp(jmpbuf, 1); // jumpout
    return;
  }

  // * p = "+D+E=Y+N+R=E+E+O=N+S+M=O=M";
  c = *(p + 1);
  switch (*p) {
  // * incase its an addend string.
  case '+':
    // if c already has a numerical value calculated
    if (sel[c] >= 0) {
      /* already selected */
      // * find the value of next character
      solve(p + 2, s + sel[c]);
    } else {
      /* not selected yet */
      // * get the lowest possible value of the char
      for (d = lower[c]; d < BASE; ++d) {
        // * if that value is used, continue.
        if (used[d])
          continue;
        // * digit value not used, try to solve using d as value of c.
        used[d] = YES;
        sel[c] = d;

        solve(p + 2, s + d);
        // * unuse that particular value, move on the next possible value.
        used[d] = NO;
        sel[c] = -1;
      }
    }
    break;
  // * incase its a sum string.
  case '=':
    d = mod(s, BASE); // * calculate the value of char
    if (sel[c] >= 0) {
      /* already selected */
      if (sel[c] == d) {
        // we have calculated sum of one column, so we divide the total sum by
        // 10, and move to the next column
        solve(p + 2, quot(s, BASE));
      }
    } else {
      /* not selected yet */
      if (!used[d] && d >= lower[c]) {
        used[d] = YES;
        sel[c] = d;
        solve(p + 2, quot(s, BASE));
        used[d] = NO;
        sel[c] = -1;
      }
    }
    break;
  }
  return;
}

void show_puzzle() {
  char **pp;
  for (pp = puzzle; *pp != NULL; ++pp) {
    if (*(pp + 1) == NULL)
      putstr("=");
    else if (pp > puzzle)
      putstr("+");
    putstr(*pp);
  }
}

void show_sol() {
  char **pp, *p;
  for (pp = puzzle; *pp != NULL; ++pp) {
    if (*(pp + 1) == NULL)
      putstr("=");
    else if (pp > puzzle)
      putstr("+");
    for (p = *pp; *p; ++p)
      printf("%d", sel[(unsigned)*p]);
  }
}

long usertime() { return (1000.0 * clock() / CLOCKS_PER_SEC); }

/*
 Local Variables:
 mode: c
 c-basic-offset: 4
 End:
*/
// {
// 	\rtf1
// }