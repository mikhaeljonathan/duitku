package duitku.project.se.firebase;

import android.content.Context;

import duitku.project.se.budget.Budget;
import duitku.project.se.budget.BudgetController;
import duitku.project.se.category.Category;
import duitku.project.se.category.CategoryController;
import duitku.project.se.notification.NotificationController;
import duitku.project.se.transaction.Transaction;
import duitku.project.se.transaction.TransactionController;
import duitku.project.se.user.UserController;
import duitku.project.se.wallet.Wallet;
import duitku.project.se.wallet.WalletController;

import java.util.List;

public class FirebaseWriter {
    // ini buat write semua data dari user yg logged in ke sub collectionnya
    // sekali panggil classnya lgsg exec semua write
    // panggilnya pake jobscheduller 6 jam sekali

    private final Context context;
    private final FirebaseHelper fbHelper;

    public FirebaseWriter(Context context) {
        this.context = context;
        fbHelper = new FirebaseHelper();
    }

    public void writeAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeAllArticles();
                writeAllBudgets();
                writeAllCategories();
                writeAllTransactions();
                writeAllWallets();
                writeUser();
                new NotificationController(context).sendOnChannelBackupDatabase();
            }
        }).start();
    }

    private void writeAllArticles() {
//        List<Article> all = new ArrayList<>();
//        //TODO masukin semua artikel dari SQLite ke all
//
//        for (Article x: all) {
//            fbHelper.article_col_ref.document("" + x.getId()).set(x.toHashmap());
//        }
    }

    private void writeAllBudgets() {
        BudgetController budgetController = new BudgetController(context);
        List<Budget> allBudget = budgetController.getAllBudget();

        for (Budget budget : allBudget) {
            fbHelper.addBudgetToFirebase(budgetController.convertBudgetToHashMap(budget));
        }
    }

    private void writeAllCategories() {
        CategoryController categoryController = new CategoryController(context);
        List<Category> allCategory = categoryController.getAllCategory();

        for (Category category : allCategory) {
            fbHelper.addCategoryToFirebase(categoryController.convertCategoryToHashMap(category));
        }
    }

    private void writeAllTransactions() {
        TransactionController transactionController = new TransactionController(context);
        List<Transaction> allTransaction = transactionController.getAllTransaction();

        for (Transaction transaction : allTransaction) {
            fbHelper.addTransactionToFirebase(transactionController.convertTransactionToHashMap(transaction));
        }
    }

    private void writeAllWallets() {
        WalletController walletController = new WalletController(context);
        List<Wallet> allWallet = walletController.getAllWallet();

        for (Wallet wallet : allWallet) {
            fbHelper.addWalletToFirebase(walletController.convertWalletToHashMap(wallet));
        }
    }

    public void writeUser() {
        UserController userController = new UserController(context);
        fbHelper.addUserToFirebase(userController.convertUserToHashMap(userController.getUser()));
    }

    public void deleteBudget(long budgetId) {
        fbHelper.getBudgetRef().document("" + budgetId).delete();
    }

    public void deleteCategory(long categoryId) {
        fbHelper.getCategoryRef().document("" + categoryId).delete();
    }

    public void deleteTransaction(long transactionId) {
        fbHelper.getTransactionRef().document("" + transactionId).delete();
    }

    public void deleteWallet(long walletId) {
        fbHelper.getWalletRef().document("" + walletId).delete();
    }

}