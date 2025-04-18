package com.example.langup.models;

import java.util.List;

public class Lesson {
    private String id;
    private Metadata metadata;
    private Script script;
    private Questions questions;
    private Grammar grammar;
    private Vocabulary vocabulary;

    public Lesson() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }

    public Questions getQuestions() {
        return questions;
    }

    public void setQuestions(Questions questions) {
        this.questions = questions;
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public void setGrammar(Grammar grammar) {
        this.grammar = grammar;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public static class Metadata {
        private String title;
        private String description;
        private int level;
        private String category;
        private String imagePath;
        private String youtubeUrl;

        public Metadata() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getYoutubeUrl() {
            return youtubeUrl;
        }

        public void setYoutubeUrl(String youtubeUrl) {
            this.youtubeUrl = youtubeUrl;
        }
    }

    public static class Script {
        private List<ScriptLine> lines;

        public Script() {
        }

        public List<ScriptLine> getLines() {
            return lines;
        }

        public void setLines(List<ScriptLine> lines) {
            this.lines = lines;
        }

        public static class ScriptLine {
            private String character;
            private String text;

            public ScriptLine() {
            }

            public String getCharacter() {
                return character;
            }

            public void setCharacter(String character) {
                this.character = character;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }
    }

    public static class Questions {
        private List<Question> items;

        public Questions() {
        }

        public List<Question> getItems() {
            return items;
        }

        public void setItems(List<Question> items) {
            this.items = items;
        }

        public static class Question {
            private String id;
            private String text;
            private String type; // "single_choice" или "multiple_choice"
            private List<String> options;
            private int correctAnswer; // для single_choice
            private List<Integer> correctAnswers; // для multiple_choice

            public Question() {
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<String> getOptions() {
                return options;
            }

            public void setOptions(List<String> options) {
                this.options = options;
            }

            public int getCorrectAnswer() {
                return correctAnswer;
            }

            public void setCorrectAnswer(int correctAnswer) {
                this.correctAnswer = correctAnswer;
            }

            public List<Integer> getCorrectAnswers() {
                return correctAnswers;
            }

            public void setCorrectAnswers(List<Integer> correctAnswers) {
                this.correctAnswers = correctAnswers;
            }
        }
    }

    public static class Grammar {
        private String title;
        private String instructions;
        private List<GrammarExercise> exercises;

        public Grammar() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public List<GrammarExercise> getExercises() {
            return exercises;
        }

        public void setExercises(List<GrammarExercise> exercises) {
            this.exercises = exercises;
        }

        public static class GrammarExercise {
            private String id;
            private String sentence;
            private String correctAnswer;
            private int position;

            public GrammarExercise() {
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSentence() {
                return sentence;
            }

            public void setSentence(String sentence) {
                this.sentence = sentence;
            }

            public String getCorrectAnswer() {
                return correctAnswer;
            }

            public void setCorrectAnswer(String correctAnswer) {
                this.correctAnswer = correctAnswer;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }
        }
    }

    public static class Vocabulary {
        private List<VocabularyItem> items;

        public Vocabulary() {
        }

        public List<VocabularyItem> getItems() {
            return items;
        }

        public void setItems(List<VocabularyItem> items) {
            this.items = items;
        }

        public static class VocabularyItem {
            private String id;
            private String word;
            private String definition;
            private String translation;

            public VocabularyItem() {
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }

            public String getDefinition() {
                return definition;
            }

            public void setDefinition(String definition) {
                this.definition = definition;
            }

            public String getTranslation() {
                return translation;
            }

            public void setTranslation(String translation) {
                this.translation = translation;
            }
        }
    }
} 