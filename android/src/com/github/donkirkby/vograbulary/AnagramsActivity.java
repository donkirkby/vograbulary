package com.github.donkirkby.vograbulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.github.donkirkby.vograbulary.anagrams.AnagramsGameModel;
import com.github.donkirkby.vograbulary.anagrams.AnagramsPlayer;
import com.github.donkirkby.vograbulary.anagrams.InvalidWordException;

public class AnagramsActivity extends VograbularyActivity {
    private static final double TILE_IN_WORD_SCALE_FACTOR = 0.6;
//  private static final float MIN_DRAG_ROTATION = -8f; // degrees
//  private static final float MAX_DRAG_ROTATION = 8f;
//  private static final int ANIMATION_DURATION = 400; // milliseconds
//  private static final int TILE_SHADOW_RADIUS = 10;
//  private static final int COLOR_INVISIBLE = 0x00000000;
//  private static final int PLACEHOLDER_UNUSED = -1;
////    private final static double MIN_HEIGHT_TO_WIDTH = 1.4;
//

    private AnagramsGameModel mGameModel;
//
    private List<LetterDisplay> mUnclaimed = new ArrayList<>();
    private List<LetterDisplay> mActiveWord = new ArrayList<>();
    private List<LetterDisplay> mCapturedWord;
    private HashMap<AnagramsPlayer, Rect> mPlayerBuildingAreas =
            new HashMap<>();
    private HashMap<AnagramsPlayer, List<List<LetterDisplay>>> mPlayerWords = 
            new HashMap<>();
    private AnagramsPlayer mActivePlayer;
    private int mLongestWordSize;
    private int mMaxWordCount; // most words owned by a single player
    private LetterDisplay mPlaceHolderTile;
//  private int mPlaceHolderIndex = PLACEHOLDER_UNUSED;
//
    private int mTileWidthInWord;
    private int mTileWidthInGrid;
//
//  private int mGridTop;
    private int mWordTop;
//  private int mAddToWordThreshold;
//
//  private static Random mRand = new Random();
//
//  private enum DragAnimation {
//      NONE, EXPANDING, CONTRACTING
//  };
//
//  private DragAnimation mDragAnimation;
//  private ExpandContractAnimation mExpandContractAnimation;
//
    private int mEvenTileColor;
    private int mOddTileColor;
    private int mPlayer1Color;
    private int mPlayer2Color;
    private int mPlayer1SurroundedColor;
    private int mPlayer2SurroundedColor;
    private int mTextColor;
    private int mNextColor;
    private int mClearColor;
    private int mDropShadowColor;
//
    private Button mSubmitButton;
    private Button mNextButton;
    private Button mClearButton;
    private Button mPlayer1Button;
    private Button mPlayer2Button;
    private TextView mMessage;
//  TextWidget mPlayer1Score;
//  TextWidget mPlayer2Score;
    private AndroidLetterDisplayFactory letterDisplayFactory;
    private LetterDisplay.LetterDisplayListener letterListener =
            new LetterDisplay.LetterDisplayListener() {
        @Override
        public void onClick(LetterDisplay letter) {
            onClickLetter(letter);
        }
    };
//
//  public AnagramsBoard(Context context, AnagramsGameModel gameModel) {
//      super(context);
//      mGameModel = gameModel;
//      init();
//  }
//
//  public AnagramsBoard(Context context, AttributeSet attrs) {
//      super(context, attrs);
//      init();
//  }
//
//  public AnagramsBoard(Context context, AttributeSet attrs, int defStyle) {
//      super(context, attrs, defStyle);
//      init();
//  }
//
    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagrams);

        Resources res = getResources();
        mEvenTileColor = res.getColor(R.color.tile_even);
        mOddTileColor = res.getColor(R.color.tile_odd);
        mPlayer1Color = res.getColor(R.color.player1);
        mPlayer2Color = res.getColor(R.color.player2);
        mPlayer1SurroundedColor = res.getColor(R.color.player1_surrounded);
        mPlayer2SurroundedColor = res.getColor(R.color.player2_surrounded);
        mTextColor = res.getColor(R.color.tile_text);
        mNextColor = Color.GREEN;
        mClearColor = Color.rgb(200, 0, 200);
        mDropShadowColor = res.getColor(R.color.tile_dropshadow);
        rootLayout = (ViewGroup)findViewById(R.id.anagramsLayout);
        mPlayer1Button = (Button)findViewById(R.id.player1);
        mPlayer2Button = (Button)findViewById(R.id.player2);
        mNextButton = (Button)findViewById(R.id.next);
        mSubmitButton = (Button)findViewById(R.id.submit);
        mClearButton = (Button)findViewById(R.id.clear);
        mMessage = (TextView)findViewById(R.id.message);
        
        letterDisplayFactory = new AndroidLetterDisplayFactory(rootLayout);

        mGameModel = new AnagramsGameModel();
        mGameModel.setWordList(loadWordList());
        String letterSource = 
                "AAAAAAAAAAAAAAAABBBBCCCCDDDDDDDDEEEEEEEEEEEEEEEEEEEEEEFFFF" +
                "GGGGGGHHHHHHIIIIIIIIIIIIIIJJKKLLLLLLLLMMMMNNNNNNNNNN" +
                "OOOOOOOOOOOOOOPPPPQQRRRRRRRRRRRRSSSSSSSSTTTTTTTTTTUUUUUUUU" +
                "VVWWXXYYYYZZ";
        char[] letters = letterSource.toCharArray();
        // shuffle
        for (int i = letters.length - 1; i > 0; i--) {
            // int from remainder of deck
            int r = (int) (Math.random() * (i + 1));
            char swap = letters[r];
            letters[r] = letters[i];
            letters[i] = swap;
        }

        mGameModel.setDeck(new String(letters));
        mGameModel.addPlayer(new AnagramsPlayer());
        mGameModel.addPlayer(new AnagramsPlayer());
        for (int i = 0; i < 4; i++) {
            mGameModel.revealLetter();
        }
        String unclaimed = mGameModel.getUnclaimedLetters();
        final List<AnagramsPlayer> players = mGameModel.getPlayers();
        for (AnagramsPlayer player : players) {
            List<List<LetterDisplay>> playerWordList = new ArrayList<>();
            mPlayerWords.put(player, playerWordList);
            List<String> words = mGameModel.getWords(player);
            for (String word : words) {
                List<LetterDisplay> wordTiles = new ArrayList<>();
                playerWordList.add(wordTiles);
                for (int i = 0; i < word.length(); i++) {
                    LetterDisplay tile = addTile(word.substring(i, i + 1));
                    wordTiles.add(tile);
                }
            }
        }
              
        for (int i = 0; i < unclaimed.length(); i++) {
            mUnclaimed.add(addTile(unclaimed.substring(i, i + 1)));
        }
              
        mPlaceHolderTile = addTile("");
        mPlayer1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivePlayer == null) {
                    setActivePlayer(players.get(0));
                }
            }
        });
        mPlayer2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivePlayer == null) {
                    setActivePlayer(players.get(1));
                }
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mActivePlayer != null) {
                    submitWord();
                }
            }

        });

        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mActivePlayer == null) {
                    revealLetter();
                }
            }

        });
      
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivePlayer != null) {
                    setActivePlayer(null);
                }
            }
        });
        setActivePlayer(null);
//
//      String player1Score = String.valueOf(
//              players.get(0).getScore());
//      String player2Score = String.valueOf(
//              players.get(1).getScore());
//
//      mPlayer1Score = new TextWidget(COLOR_INVISIBLE, player1Score,
//              mPlayer1SurroundedColor);
//      mPlayer2Score = new TextWidget(COLOR_INVISIBLE, player2Score,
//              mPlayer2SurroundedColor);
//      addWidget(mPlayer1Score);
//      addWidget(mPlayer2Score);
//  }
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    private int previousWidth;
                    private int previousHeight;

                    @Override
                    public void onGlobalLayout() {
                        int width = rootLayout.getWidth();
                        int height = rootLayout.getHeight();
                        if (width != previousWidth || height != previousHeight) {
                            previousWidth = width;
                            previousHeight = height;
                            layoutBoard();
                        }
                    }
                });
    }

    private LetterDisplay addTile(String letter) {
        LetterDisplay tile = letterDisplayFactory.create(letter);
        tile.addClickListener(letterListener);
//        tile.setDragListener(this);
        return tile;
    }
//
//  @Override
//  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//      // Enforce a minimum height to width ratio for the board
//
//      int measuredWidth = getDefaultSize(getSuggestedMinimumWidth(),
//              widthMeasureSpec);
//      int measuredHeight = getDefaultSize(getSuggestedMinimumHeight(),
//              heightMeasureSpec);
//
////        final double measuredHeightToWidthRatio = (double) measuredHeight
////                / measuredWidth;
////
////        if (measuredHeightToWidthRatio < MIN_HEIGHT_TO_WIDTH) {
////            setMeasuredDimension((int) (measuredHeight / MIN_HEIGHT_TO_WIDTH),
////                    measuredHeight);
////        } else {
//          setMeasuredDimension(measuredWidth, measuredHeight);
////        }
//
//  }
//
//  @Override
//  protected void onLayout(boolean changed, int left, int top, int right,
//          int bottom) {
//      super.onLayout(changed, left, top, right, bottom);
//      layoutBoard();
//  }
//
    private void layoutBoard() {
        mLongestWordSize = 7; // Always leave room for minimum letters.
        mMaxWordCount = Math.max(5, // Always leave room for minimum words.
                (mUnclaimed.size() + 2) / 3); // leave room for all unclaimed
                                              // letters.

        for (List<List<LetterDisplay>> words : mPlayerWords.values()) {
            // leave room for building a new word.
            mMaxWordCount = Math.max(mMaxWordCount, words.size() + 1);
            for (List<LetterDisplay> word : words) {
                mLongestWordSize = Math.max(mLongestWordSize, word.size());
            }
        }

        int displayWidth = rootLayout.getWidth();
        int displayHeight = rootLayout.getHeight();
        int tileWidth = displayWidth
                / 2
                / (int) Math.round(mLongestWordSize + 2
                        / TILE_IN_WORD_SCALE_FACTOR);
        int tileHeight = displayHeight / (mMaxWordCount + 1);
        if (tileHeight > (int) (tileWidth / TILE_IN_WORD_SCALE_FACTOR)) {
            tileHeight = (int) (tileWidth / TILE_IN_WORD_SCALE_FACTOR);
        } else {
            tileWidth = (int) (tileHeight * TILE_IN_WORD_SCALE_FACTOR);
        }

        mTileWidthInGrid = tileHeight;
        mTileWidthInWord = tileWidth;

        AnagramsPlayer player1 = mGameModel.getPlayers().get(0);
        AnagramsPlayer player2 = mGameModel.getPlayers().get(1);
        mPlayerBuildingAreas.put(player1, new Rect(0, 0, displayWidth / 2 - 2
                * tileHeight, tileHeight));
        mPlayerBuildingAreas.put(player2, new Rect(displayWidth / 2 + 2
                * tileHeight, 0, displayWidth, tileHeight));

        for (int i = 0; i < mUnclaimed.size(); ++i) {
            int row = i / 3;
            int col = i % 3;
            int x = (displayWidth + (2 * col - 3) * mTileWidthInGrid) / 2;
            int y = tileHeight * row;
            LetterDisplay letterDisplay = mUnclaimed.get(i);
            letterDisplay.animateTo(x, y);
//            letterDisplay.setLeft(x);
//            letterDisplay.setTop(y);
            letterDisplay.setHomeLeft(x);
            letterDisplay.setHomeTop(y);
        }

        layoutPlayerWords(mGameModel.getPlayers().get(0), 0, tileHeight);
        layoutPlayerWords(
                mGameModel.getPlayers().get(1),
                displayWidth / 2 + 2 * tileHeight,
                tileHeight);
//
//        mPlaceHolderTile.applyLayout(0, mWordTop, mTileWidthInWord, tileHeight);
//
//        mPlayer1Button.applyLayout(0, displayHeight - tileHeight, tileHeight,
//                tileHeight);
//        mPlayer2Button.applyLayout(displayWidth - tileHeight, displayHeight
//                - tileHeight, tileHeight, tileHeight);
//        mPlayer1Score.applyLayout(displayWidth / 4 - tileHeight, displayHeight
//                - tileHeight, tileHeight * 2, tileHeight);
//        mPlayer2Score.applyLayout(displayWidth * 3 / 4, displayHeight - tileHeight,
//                tileHeight * 2, tileHeight);
//        mSubmitButton.applyLayout((displayWidth - 3 * tileHeight) / 2,
//                displayHeight - tileHeight, tileHeight, tileHeight);
//        mNextButton.applyLayout((displayWidth - tileHeight) / 2, displayHeight
//                - tileHeight, tileHeight, tileHeight);
//        mClearButton.applyLayout((displayWidth + tileHeight) / 2, displayHeight
//                - tileHeight, tileHeight, tileHeight);
    }

    private void layoutPlayerWords(
            AnagramsPlayer player,
            int left,
            int tileHeight) {
        int y = 0;
        for (List<LetterDisplay> word : mPlayerWords.get(player)) {
            y += tileHeight;
            int x = left;
            for (LetterDisplay tile : word) {
                tile.animateTo(x, y);
                x += mTileWidthInWord;
            }
        }
    }

    public void onClickLetter(LetterDisplay t) {
        Rect buildingArea = getActiveBuildingArea();
        if (buildingArea == null) {
            List<LetterDisplay> word = findOwningWord(t);
            for (AnagramsPlayer player : mGameModel.getPlayers()) {
                List<List<LetterDisplay>> words = mPlayerWords.get(player);
                if (words.contains(word)) {
                    setActivePlayer(player);
                    return;
                }
            }
            return;
        }

        if (buildingArea.contains(t.getCentreX(), t.getCentreY())) {
            removeTileFromWord(t);
            animateToPosition(
                    t,
                    t.getHomeLeft(),
                    t.getHomeTop(),
                    mTileWidthInGrid);
        } else {
            addTileToWord(t);
            List<LetterDisplay> owningWord = findOwningWord(t);
            if (owningWord != null) {
                mCapturedWord = owningWord;
            }
        }
    }

    //
//  /**
//   * Pass touch event to widgets, but if that's not a hit, see if a player
//   * is ringing in by touching the background.
//   */
//  @Override
//  public boolean onTouchEvent(MotionEvent m) {
//      if (super.onTouchEvent(m)) {
//          return true;
//      }
//      else {
//          if (mActivePlayer == null) {
//              for (AnagramsPlayer player : mGameModel.getPlayers()) {
//                  Rect buildingArea = mPlayerBuildingAreas.get(player);
//                  if (buildingArea.contains(
//                          (int) m.getX(), 
//                          buildingArea.centerY())) {
//                      setActivePlayer(player);
//                      return true;
//                  }
//              }
//          }
//          return false;
//      }
//  }
//
    private List<LetterDisplay> findOwningWord(LetterDisplay tile) {
        for (AnagramsPlayer player : mGameModel.getPlayers()) {
            for (List<LetterDisplay> word : mPlayerWords.get(player)) {
                if (word.contains(tile)) {
                    return word;
                }
            }
        }
        return null;
    }

//  @Override
//  public void onDragStart(Widget widget) {
//
//      mDragAnimation = DragAnimation.NONE;
//      mPlaceHolderIndex = PLACEHOLDER_UNUSED;
//
//      LetterDisplay tile = (LetterDisplay) widget;
//
//      removeTileFromWord(tile);
//
//      tile.setShadow(TILE_SHADOW_RADIUS, mDropShadowColor);
//
//      float angle = (mRand.nextFloat() * (Math.abs(MIN_DRAG_ROTATION) + Math
//              .abs(MAX_DRAG_ROTATION))) + MIN_DRAG_ROTATION;
//      tile.cancelAllAnimations();
//      tile.addAnimation(new RotationAnimation(tile, ANIMATION_DURATION, angle));
//  }
//
//  @Override
//  public void onDragEnd(Widget widget) {
//
//      LetterDisplay tile = (LetterDisplay) widget;
//
//      tile.setShadow(0, 0);
//
//      tile.cancelAllAnimations();
//      tile.addAnimation(new RotationAnimation(tile, ANIMATION_DURATION, 0));
//
//      if (widget.getY() <= mAddToWordThreshold
//              && mPlaceHolderIndex != PLACEHOLDER_UNUSED) {
//          mActiveWord.add(mPlaceHolderIndex, tile);
//      } else {
//          animateToPosition(tile, tile.mPositionInGrid_x,
//                  tile.mPositionInGrid_y, Tile.widthInWord);
//      }
//
//      // remove the place holder tile
//      mActiveWord.remove(mPlaceHolderTile);
//      mPlaceHolderIndex = PLACEHOLDER_UNUSED;
//      presentWord();
//
//  }
//
//  @Override
//  public void onDrag(Widget widget, int x, int y) {
//
//      LetterDisplay tile = (LetterDisplay) widget;
//
//      // Reposition tile according to drag coordinates
//      tile.setX(x);
//      tile.setY(y);
//
//      // When dragged above the grid, resize the tile to fit in the word
//      if (y < mGridTop && mDragAnimation != DragAnimation.CONTRACTING) {
//
//          mDragAnimation = DragAnimation.CONTRACTING;
//          expandContractTile(tile, Tile.widthInWord);
//
//      } else if (y >= mGridTop && mDragAnimation != DragAnimation.EXPANDING) {
//
//          mDragAnimation = DragAnimation.EXPANDING;
//          expandContractTile(tile, Tile.widthInGrid);
//
//      }
//
//      // When dragged above the grid, make space in the word for the tile
//      if (y < mGridTop) {
//
//          int word_begin = (int) (getWidth() / 2 - (Tile.widthInWord / 2.0)
//                  * mActiveWord.size());
//          int word_end = word_begin + Tile.widthInWord * mActiveWord.size();
//
//          int index;
//          if (x < word_begin) {
//              index = 0;
//          } else if (x > word_end) {
//              index = mActiveWord.size();
//          } else {
//              index = (int) (x - (word_begin - Tile.widthInWord / 2.0))
//                      / Tile.widthInWord;
//          }
//
//          if (index != mPlaceHolderIndex) {
//              mPlaceHolderIndex = index;
//              mActiveWord.remove(mPlaceHolderTile);
//
//              mActiveWord.add(Math.min(index, mActiveWord.size()), mPlaceHolderTile);
//              presentWord();
//          }
//      }
//  }
//
//  private void expandContractTile(LetterDisplay tile, int newWidth) {
//      if (mExpandContractAnimation != null)
//          tile.cancelAnimation(mExpandContractAnimation);
//      mExpandContractAnimation = new ExpandContractAnimation(tile,
//              ANIMATION_DURATION, newWidth);
//      tile.addAnimation(mExpandContractAnimation);
//  }
//
    private void animateToPosition(LetterDisplay tile, int x, int y, int width) {
        tile.animateTo(x, y);
//        tile.cancelAllAnimations();
//        tile.addAnimation(new RotationAnimation(tile, ANIMATION_DURATION, 0));
//        tile.addAnimation(new TranslationAnimation(tile, ANIMATION_DURATION, x,
//                y));
//        tile.addAnimation(new ExpandContractAnimation(tile, ANIMATION_DURATION,
//                width));
    }

    private void addTileToWord(LetterDisplay tile) {
        mActiveWord.remove(tile);
        mActiveWord.add(tile);
        presentWord();
    }

    private void removeTileFromWord(LetterDisplay tile) {
        mActiveWord.remove(tile);
        presentWord();
    }

    private void returnAllTilesToGrid() {
        ListIterator<LetterDisplay> it = mActiveWord.listIterator();
        while (it.hasNext()) {
            LetterDisplay t = it.next();
            it.remove();
            animateToPosition(
                    t,
                    t.getHomeLeft(),
                    t.getHomeTop(),
                    mTileWidthInGrid);
        }
    }

    private void presentWord() {
        Rect buildingArea = getActiveBuildingArea();
        if (buildingArea == null) {
            return;
        }
        if (mActiveWord.size() > mLongestWordSize) {
            mLongestWordSize = mActiveWord.size();
            layoutBoard();
        }

        for (int i = 0; i < mActiveWord.size(); ++i) {

            LetterDisplay t = mActiveWord.get(i);
            int x = buildingArea.left + mTileWidthInWord * i;
            int y = buildingArea.top;

            animateToPosition(t, x, y, mTileWidthInWord);
        }
    }

    private Rect getActiveBuildingArea() {
        Rect buildingArea =
                mActivePlayer == null
                ? null
                : mPlayerBuildingAreas.get(mActivePlayer);
        return buildingArea;
    }

    private void setActivePlayer(AnagramsPlayer player) {
        mActivePlayer = player;
        if (player == null) {
            returnAllTilesToGrid();
            mCapturedWord = null;
            mPlayer1Button.setVisibility(View.VISIBLE);
            mPlayer2Button.setVisibility(View.VISIBLE);
            mSubmitButton.setVisibility(View.INVISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
            mClearButton.setVisibility(View.INVISIBLE);
        } else {
            if (player == mGameModel.getPlayers().get(0)) {
                mPlayer2Button.setVisibility(View.INVISIBLE);
            } else {
                mPlayer1Button.setVisibility(View.INVISIBLE);
            }
            mSubmitButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.INVISIBLE);
            mClearButton.setVisibility(View.VISIBLE);
        }
    }

    private void submitWord() {

        final Resources res = getResources();
        final String dismiss = res.getString(R.string.dismiss_message);
        try {
            if (mCapturedWord != null) {
                mGameModel.changeWord(buildWord(mCapturedWord),
                        buildWord(mActiveWord), mActivePlayer);
                for (AnagramsPlayer player : mGameModel.getPlayers()) {
                    mPlayerWords.get(player).remove(mCapturedWord);
                }
            } else {
                mGameModel.makeWord(buildWord(mActiveWord), mActivePlayer);
            }
            mPlayerWords.get(mActivePlayer).add(mActiveWord);

            for (LetterDisplay tile : mActiveWord) {
                mUnclaimed.remove(tile);
            }
            mActiveWord = new ArrayList<LetterDisplay>();
            setActivePlayer(null);

//            mPlayer1Score.setText(String.valueOf(mGameModel.getPlayers().get(0)
//                    .getScore()));
//            mPlayer2Score.setText(String.valueOf(mGameModel.getPlayers().get(1)
//                    .getScore()));

            layoutBoard();

        } catch (InvalidWordException ex) {
            mMessage.setText(ex.getMessage());
        }

    }

    private String buildWord(List<LetterDisplay> tiles) {
        StringBuilder word = new StringBuilder();
        for (LetterDisplay tile : tiles) {
            word.append(tile.getLetter());
        }
        String wordText = word.toString();
        return wordText;
    }

    private void revealLetter() {
        if (mGameModel.isDeckEmpty()) {
            return;
        }
        char letter = mGameModel.revealLetter();
        LetterDisplay tile = addTile(String.valueOf(letter));
        mUnclaimed.add(tile);
        mMaxWordCount = Math.max(mMaxWordCount, mUnclaimed.size());
        layoutBoard();
        if (mGameModel.isDeckEmpty()) {
            mNextButton.setVisibility(View.INVISIBLE);
        }
    }
}
