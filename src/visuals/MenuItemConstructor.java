package visuals;

import algorithms.Algorithm;
import algorithms.AlgorithmHolder;
import algorithms.ECB;
import algorithms.MAC;
import analysis.Analysis;
import ciphers.CipherHolder;
import hashfunctions.HashFunction;
import hashfunctions.HashFunctionHolder;
import mac.HMAC;
import org.apache.commons.math3.util.Pair;
import widgets.FileFrame;
import workers.*;
import hashfunctions.Streebog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;


public class MenuItemConstructor {
    private static MenuItemConstructor instance;
    private final JDesktopPane target;

    private MenuItemConstructor(JDesktopPane target) {
        this.target = target;
    }

    public static MenuItemConstructor get(JDesktopPane target) {
        if (instance == null) {
            instance = new MenuItemConstructor(target);
        }
        return instance;
    }


    public enum Mode {
        ENCRYPT, DECRYPT
    }


    public JMenuItem fileCreateItem(String name, Callable<FileFrame> act) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        FileFrame frame = act.call();
                        if (frame != null) {
                            frame.display(target);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    private JMenuItem encodeItem(String name, AlgorithmHolder holder) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FileFrame selected = (FileFrame) target.getSelectedFrame();
                Algorithm algorithm = InputDialogConstructor.get(target).algorithmDialog(holder);
                GeneralExecutor executor = new GeneralExecutor(target, "Зашифрование",
                        () -> algorithm.encrypt(selected.getByteContent()),
                        (byte[] value) -> selected.process(value, "encrypted"));
                executor.execute();
            }
        });
    }

    private JMenuItem decodeItem(String name, AlgorithmHolder holder) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FileFrame selected = (FileFrame) target.getSelectedFrame();
                Algorithm algorithm = InputDialogConstructor.get(target).algorithmDialog(holder);
                GeneralExecutor executor = new GeneralExecutor(target, "Расшифрование",
                        () -> algorithm.decrypt(selected.getByteContent()),
                        (byte[] value) -> selected.process(value, "decrypted"));
                executor.execute();
            }
        });
    }

    public JMenuItem encodeDecodeItem(String name, AlgorithmHolder holder, Mode mode) {
        return mode == Mode.ENCRYPT ? encodeItem(name, holder) : decodeItem(name, holder);
    }

    public JMenuItem subscribeItem(String name, CipherHolder holder) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FileFrame selected = (FileFrame) target.getSelectedFrame();
                MAC mac = InputDialogConstructor.get(target).macDialog(holder);
                GeneralExecutor executor = new GeneralExecutor(target, "Создание кода имитовставки",
                        () -> mac.subscribe(selected.getByteContent()),
                        (byte[] value) -> selected.process(value, "subscribed"));
                executor.execute();
            }
        });
    }

    public JMenuItem analyzeItem(String name, AlgorithmHolder holder) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FileFrame selected = (FileFrame) target.getSelectedFrame();
                Analysis analysis = InputDialogConstructor.get(target).analysisDialog(holder);
                AnalysisExecutor executor = new AnalysisExecutor(analysis, selected.getByteContent(), target, (byte[] key) -> {
                    Algorithm algo = holder.instantiate(key, analysis.initialVector, analysis.padding);
                    return selected.process(algo.decrypt(selected.getByteContent()), "analyzed");
                });
                executor.execute();
            }
        });
    }


    public JMenuItem visualizeKuznyechikRoundItem(String name, CipherHolder holder) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Pair<ECB, byte[]> result = InputDialogConstructorKuznyechikRound.get(target).visualizationDialog(holder);
                VisualizationKuznyechikRoundExecutor executor = new VisualizationKuznyechikRoundExecutor(target, result.getFirst(), result.getSecond());
                executor.execute();
            }
        });
    }

    public JMenuItem visualizeMagmaRoundItem(String name, CipherHolder holder) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Pair<ECB, byte[]> result = InputDialogConstructorMagmaRound.get(target).visualizationDialog(holder);
                VisualizationExecutorMagmaRound executor = new VisualizationExecutorMagmaRound(target, result.getFirst(), result.getSecond());
                executor.execute();
            }
        });
    }

    public JMenuItem visualizeMagmaKeyItem(String name, CipherHolder holder) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Pair<ECB, byte[]> result = InputDialogConstructorExpandMagmaKey.get(target).visualizationDialog(holder);
                VisualizationExecutorMagmaKey executor = new VisualizationExecutorMagmaKey(target, result.getFirst(), result.getSecond());
                executor.execute();
            }
        });
    }

    public JMenuItem visualizeExpandKeyItem(String name, CipherHolder holder) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Pair<ECB, byte[]> result = InputDialogConstructorExpandKuznyechikKey.get(target).visualizationDialog(holder);
                VisualizationExpandKeyExecutor executor = new VisualizationExpandKeyExecutor(target, result.getFirst(), result.getSecond());
                executor.execute();
            }
        });
    }

    public JMenuItem visualizeRegister(String name, CipherHolder holder) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Pair<ECB, byte[]> result = InputDialogConstructor.get(target).visualizationDialog(holder);
                VisualizationRegisterExecutor executor = new VisualizationRegisterExecutor(target, result.getFirst(), result.getSecond());
                executor.execute();
            }
        });
    }

    public JMenuItem streebogItem(String name, int length, HashFunctionHolder hashfunction) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FileFrame selected = (FileFrame) target.getSelectedFrame();

                HashFunction streebog = hashfunction.instantiate(length);//= new Streebog(length);
                GeneralExecutor executor;
                if(length == 256) {
                    executor = new GeneralExecutor(target, "Хеширование Стрибог-256",
                            ()-> streebog.get_hash(selected.getByteContent()),
                            (byte[] value) -> selected.process(value, "streebog-256"));
                }
                else {
                    executor = new GeneralExecutor(target, "Хеширование Стрибог-512",
                            ()-> streebog.get_hash(selected.getByteContent()),
                            (byte[] value) -> selected.process(value, "streebog-512"));
                }
                executor.execute();
            }
        });
    }

    public JMenuItem visualizeStreebogItem(String name, int length, HashFunctionHolder hashfunction) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FileFrame selected = (FileFrame) target.getSelectedFrame();
                HashFunction streebog = hashfunction.instantiateWithState(length);
                VisualizationStreebogExecutor executor;
                if(length == 256) {
                    executor = new VisualizationStreebogExecutor(target, selected.getByteContent(), streebog);
                }
                else {
                    executor = new VisualizationStreebogExecutor(target, selected.getByteContent(), streebog);
                }
                executor.execute();
            }
        });
    }

    public JMenuItem HMACItem(String name, int length) {
        return new JMenuItem(new AbstractAction(name) {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FileFrame selected = (FileFrame) target.getSelectedFrame();

                Streebog streebog = new Streebog(length);
                HMAC hmac = InputDialogConstructor.get(target).hmacDialog(streebog, selected.getByteContent());
                GeneralExecutor executor = new GeneralExecutor(target, "HMAC-256",
                        hmac::computeHMAC, (byte[] value) -> selected.process(value, "HMAC-256"));
                executor.execute();
            }
        });
    }
}
