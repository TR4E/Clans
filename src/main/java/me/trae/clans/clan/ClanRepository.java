package me.trae.clans.clan;

import me.trae.clans.clan.enums.ClanProperty;
import me.trae.framework.shared.database.query.types.DeleteQuery;
import me.trae.framework.shared.database.query.types.MultiCallbackQuery;
import me.trae.framework.shared.database.query.types.SaveQuery;
import me.trae.framework.shared.database.query.types.UpdateQuery;
import me.trae.framework.shared.database.repository.Repository;
import me.trae.framework.shared.database.repository.types.MultiLoadRepository;
import me.trae.framework.shared.database.repository.types.UpdateRepository;
import me.trae.framework.shared.utility.objects.EnumData;

public class ClanRepository extends Repository<ClanManager> implements UpdateRepository<Clan, ClanProperty>, MultiLoadRepository {

    public ClanRepository(final ClanManager manager) {
        super(manager, "Clans");
    }

    @Override
    public void saveData(final Clan clan) {
        final SaveQuery<ClanProperty> query = new SaveQuery<ClanProperty>(clan.getName()) {
            @Override
            public EnumData<ClanProperty> getData() {
                return clan.getData();
            }
        };

        this.addQuery(query);
    }

    @Override
    public void updateData(final Clan clan, final ClanProperty property) {
        final UpdateQuery<ClanProperty> query = new UpdateQuery<>(clan.getName(), property, clan.getPropertyByValue(property));

        this.addQuery(query);
    }

    @Override
    public void deleteData(final Clan clan) {
        final DeleteQuery query = new DeleteQuery(clan.getName());

        this.addQuery(query);
    }

    @Override
    public void loadData() {
        final MultiCallbackQuery<ClanProperty> query = new MultiCallbackQuery<ClanProperty>() {
            @Override
            public void onCallback(final EnumData<ClanProperty> data) {
                final Clan clan = new Clan(data);

                getManager().addClan(clan);

                patchData(clan, data);

                incrementCount();
            }
        };

        this.addQuery(query);
    }
}